package gr.aueb.radio.services;

import gr.aueb.radio.domains.*;
import gr.aueb.radio.dto.BroadcastSearchDTO;
import gr.aueb.radio.dto.SuggestionsDTO;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.AdMapper;
import gr.aueb.radio.mappers.BroadcastMapper;
import gr.aueb.radio.mappers.OutputBroadcastMapper;
import gr.aueb.radio.mappers.SongMapper;
import gr.aueb.radio.persistence.*;
import gr.aueb.radio.representations.BroadcastOutputRepresentation;
import gr.aueb.radio.representations.BroadcastRepresentation;
import gr.aueb.radio.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequestScoped
@Slf4j
public class BroadcastService {
    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    BroadcastMapper broadcastMapper;

    @Inject
    SongMapper songMapper;

    @Inject
    AdMapper adMapper;

    @Inject
    OutputBroadcastMapper outputBroadcastMapper;

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    PlaylistService playlistService;

    @Inject
    SuggestionsService suggestionsService;


    @Transactional
    public Broadcast findById(Integer id){
        Broadcast broadcast = broadcastRepository.findById(id);
        if (broadcast == null){
            throw new NotFoundException("Broadcast not found");
        }
        return broadcast;
    }

    @Transactional
    public boolean checkForOverlapping(LocalDate date, LocalTime startingTime, LocalTime endingTime, Integer idToExclude){
        List<Broadcast> broadcastsOfDay;
        if (idToExclude == -1){
            broadcastsOfDay = broadcastRepository.findByDate(date);
        }else {
            broadcastsOfDay = broadcastRepository.findByDateExcluding(date, idToExclude);
        }
        // starting datetime of broadcast to be updated
        LocalDateTime startingDateTime = LocalDateTime.of(date, startingTime);
        // starting datetime of broadcast to be updated
        LocalDateTime endingDateTime = LocalDateTime.of(date, endingTime);
        for (Broadcast b: broadcastsOfDay){
            LocalDateTime start = LocalDateTime.of(date, b.getStartingTime());
            LocalDateTime end = b.getBroadcastEndingDateTime();
            if (DateUtil.between(start, startingDateTime, end) || DateUtil.between(start, endingDateTime, end)){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Broadcast create(BroadcastRepresentation broadcastRepresentation){
        Broadcast broadcastToCreate = broadcastMapper.toModel(broadcastRepresentation);
        LocalDate date = broadcastToCreate.getStartingDate();
        LocalTime startingTime = broadcastToCreate.getStartingTime();
        LocalTime endingTime = broadcastToCreate.getBroadcastEndingDateTime().toLocalTime();
        if (checkForOverlapping(date, startingTime, endingTime, -1)){
            throw new RadioException("Overlapping found cannot create Broadcast");
        }
        if(broadcastToCreate.getType() == BroadcastEnum.PLAYLIST){
            playlistService.populateBroadcast(broadcastToCreate);
        }
        broadcastRepository.persist(broadcastToCreate);
        return broadcastToCreate;
    }

    @Transactional
    public Broadcast update(Integer id, BroadcastRepresentation broadcastRepresentation){
        // try to find broadcast, if broadcast is not found, find func will throw NotFoundException
        Broadcast broadcast = this.findById(id);
        int addBroadcastSize = broadcast.getAdBroadcasts().size();
        int songBroadcastSize = broadcast.getSongBroadcasts().size();
        // Broadcast is immutable because it has registered song/add broadcasts
        if (addBroadcastSize != 0 || songBroadcastSize != 0){
            throw new RadioException("Broadcast has songs/adds scheduled, cannot be updated");
        }
        LocalDate date = DateUtil.setDate(broadcastRepresentation.startingDate);
        LocalTime startingTime = DateUtil.setTime(broadcastRepresentation.startingTime);
        LocalTime endingTime = startingTime.plusMinutes(broadcastRepresentation.duration);
        // Cannot update broadcast because the updated values will overlap another broadcast
        if(checkForOverlapping(date, startingTime, endingTime, broadcast.getId())){
            throw new RadioException("Overlapping found cannot update Broadcast");
        }
        broadcast = updateValues(broadcast, broadcastRepresentation);
        broadcastRepository.getEntityManager().merge(broadcast);
        return broadcast;
    }

    @Transactional
    public void delete(Integer id){
        Broadcast broadcast = this.findById(id);
        while (broadcast.getAdBroadcasts().size() != 0){
            AdBroadcast ab = broadcast.getAdBroadcasts().get(0);
            this.removeAdBroadcast(broadcast.getId(), ab.getId());
        }
        while (broadcast.getSongBroadcasts().size() != 0){
            SongBroadcast sb = broadcast.getSongBroadcasts().get(0);
            this.removeSongBroadcast(broadcast.getId(), sb.getId());
        }
        broadcastRepository.deleteById(id);
    }

    @Transactional
    public List<BroadcastOutputRepresentation> search(BroadcastSearchDTO broadcastSearchDTO){
        List<Broadcast> broadcasts;
        if(broadcastSearchDTO.getDate() == null && broadcastSearchDTO.getType() == null){
            broadcasts = broadcastRepository.findByTimeRange(broadcastSearchDTO.getFromTime(), broadcastSearchDTO.getToTime());
        } else if (broadcastSearchDTO.getDate() == null) {
            broadcasts = broadcastRepository.findByTimeRangeType(broadcastSearchDTO.getFromTime(), broadcastSearchDTO.getToTime(), broadcastSearchDTO.getType());
        } else if (broadcastSearchDTO.getType() == null) {
            broadcasts = broadcastRepository.findByTimeRangeDate(broadcastSearchDTO.getFromTime(), broadcastSearchDTO.getToTime(), broadcastSearchDTO.getDate());
        } else {
            broadcasts = broadcastRepository.findByTimeRangeDateType(broadcastSearchDTO.getFromTime(), broadcastSearchDTO.getToTime(), broadcastSearchDTO.getDate(), broadcastSearchDTO.getType());
        }
        return outputBroadcastMapper.toRepresentationList(broadcasts);
    }

    @Transactional
    public AdBroadcast scheduleAd(Integer id, Ad ad, LocalTime startingTime){
        Broadcast broadcast = broadcastRepository.findByIdAdDetails(id);
        if (broadcast == null){
            throw new NotFoundException("Broadcast not found");
        }
        AdBroadcast created = broadcast.createAdBroadcast(ad, startingTime);
        if (created == null){
            throw new RadioException("Add cannot be scheduled to broadcast");
        }
        adBroadcastRepository.persist(created);
        return created;
    }

    @Transactional
    public SongBroadcast scheduleSong(Integer id, Song song, LocalTime startingTime){
        Broadcast broadcast = broadcastRepository.findByIdSongDetails(id);
        if (broadcast == null){
            throw new NotFoundException("Broadcast not found");
        }
        SongBroadcast created = broadcast.createSongBroadcast(song, startingTime);
        if (created == null){
            throw new RadioException("Song cannot be scheduled to broadcast");
        }
        songBroadcastRepository.persist(created);
        return created;
    }

    @Transactional
    public void removeAdBroadcast(Integer id, Integer abId){
        Broadcast broadcast = broadcastRepository.findByIdAdDetails(id);
        if (broadcast == null){
            throw new NotFoundException("Broadcast not found");
        }
        AdBroadcast adBroadcast = adBroadcastRepository.findById(abId);
        if(adBroadcast == null){
            throw new NotFoundException("Add Broadcast not found");
        }
        broadcast.removeAdBroadcast(adBroadcast);
        adBroadcastRepository.deleteById(abId);
    }

    @Transactional
    public void removeSongBroadcast(Integer id, Integer sbId){
        Broadcast broadcast = broadcastRepository.findByIdSongDetails(id);
        if (broadcast == null){
            throw new NotFoundException("Broadcast not found");
        }
        SongBroadcast songBroadcast = songBroadcastRepository.findById(sbId);
        if(songBroadcast == null){
            throw new NotFoundException("Song Broadcast not found");
        }
        broadcast.removeSongBroadcast(songBroadcast);
        songBroadcastRepository.deleteById(sbId);
    }

    @Transactional
    public SuggestionsDTO suggestions(Integer id){
        SuggestionsDTO suggestions = new SuggestionsDTO();
        List<Ad> ads = suggestionsService.suggestAds(id);
        suggestions.ads = adMapper.toRepresentationList(ads);
        List<Song> songs = suggestionsService.suggestSongs(id);
        suggestions.songs = songMapper.toRepresentationList(songs);
        return suggestions;
    }

    private Broadcast updateValues(Broadcast broadcast, BroadcastRepresentation broadcastRepresentation){
        broadcast.setType(broadcastRepresentation.type);
        LocalDate date = DateUtil.setDate(broadcastRepresentation.startingDate);
        LocalTime time = DateUtil.setTime(broadcastRepresentation.startingTime);
        broadcast.setStartingTime(time);
        broadcast.setStartingDate(date);
        broadcast.setDuration(broadcastRepresentation.duration);
        return broadcast;
    }
}
