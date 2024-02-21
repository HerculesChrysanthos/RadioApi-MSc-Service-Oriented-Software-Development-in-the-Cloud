package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.NotFoundRadioException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.persistence.SongBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastMapper;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastRepresentation;
import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.OutputBroadcastMapper;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

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
//
//    @Inject
//    SongMapper songMapper;
//
//    @Inject
//    AdMapper adMapper;
//
    @Inject
    OutputBroadcastMapper outputBroadcastMapper;

    // better each service to interact with another service. Not with the repository of other entity
    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    SongBroadcastRepository songBroadcastRepository;
//

//    @Inject
//    PlaylistService playlistService;
//
//    @Inject
//    SuggestionsService suggestionsService;


    @Transactional
    public Broadcast findById(Integer id){
        Broadcast broadcast = broadcastRepository.findById(id);
        if (broadcast == null){
            throw new NotFoundRadioException("Broadcast not found");
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

//    @Transactional
//    public Broadcast create(BroadcastRepresentation broadcastRepresentation){
//        Broadcast broadcastToCreate = broadcastMapper.toModel(broadcastRepresentation);
//        LocalDate date = broadcastToCreate.getStartingDate();
//        LocalTime startingTime = broadcastToCreate.getStartingTime();
//        LocalTime endingTime = broadcastToCreate.getBroadcastEndingDateTime().toLocalTime();
//
//        //verify auth user producer role
//        String userRole = userService.verifyAuth(auth).role;
//        if(!userRole.equals("PRODUCER")){
//            throw new RadioException("Not Allowed to change this.", 403);
//        }
//
//        if (checkForOverlapping(date, startingTime, endingTime, -1)){
//            throw new RadioException("Overlapping found cannot create Broadcast");
//        }
//        if(broadcastToCreate.getType().(BroadcastType.PLAYLIST)){
//            playlistService.populateBroadcast(broadcastToCreate);
//        }
//
//        broadcastRepository.persist(broadcastToCreate);
//        return broadcastToCreate;
//    }

//    @Transactional
//    public Broadcast update(Integer id, BroadcastRepresentation broadcastRepresentation){
//        // try to find broadcast, if broadcast is not found, find func will throw NotFoundException
//        String userRole = userService.verifyAuth(auth).role;
//
//        if(!userRole.equals("PRODUCER")){
//            throw new RadioException("Not Allowed to change this.", 403);
//        }
//        Broadcast broadcast = this.findById(id);
//        int addBroadcastSize = broadcast.getAdBroadcasts().size();
//        int songBroadcastSize = broadcast.getSongBroadcasts().size();
//        // Broadcast is immutable because it has registered song/add broadcasts
//        if (addBroadcastSize != 0 || songBroadcastSize != 0){
//            throw new RadioException("Broadcast has songs/adds scheduled, cannot be updated");
//        }
//        LocalDate date = DateUtil.setDate(broadcastRepresentation.startingDate);
//        LocalTime startingTime = DateUtil.setTime(broadcastRepresentation.startingTime);
//        LocalTime endingTime = startingTime.plusMinutes(broadcastRepresentation.duration);
//        // Cannot update broadcast because the updated values will overlap another broadcast
//        if(checkForOverlapping(date, startingTime, endingTime, broadcast.getId())){
//            throw new RadioException("Overlapping found cannot update Broadcast");
//        }
//        broadcast = updateValues(broadcast, broadcastRepresentation);
//        broadcastRepository.getEntityManager().merge(broadcast);
//        return broadcast;
//    }

//    @Transactional
//    public void delete(Integer id){
//        Broadcast broadcast = this.findById(id);
//        while (broadcast.getAdBroadcasts().size() != 0){
//            AdBroadcast ab = broadcast.getAdBroadcasts().get(0);
//            this.removeAdBroadcast(broadcast.getId(), ab.getId());
//        }
//        while (broadcast.getSongBroadcasts().size() != 0){
//            SongBroadcast sb = broadcast.getSongBroadcasts().get(0);
//            this.removeSongBroadcast(broadcast.getId(), sb.getId());
//        }
//        broadcastRepository.deleteById(id);
//    }

//    @Transactional
//    public List<BroadcastOutputRepresentation> search(BroadcastSearchDTO broadcastSearchDTO){
//        List<Broadcast> broadcasts;
//        if(broadcastSearchDTO.getDate() == null && broadcastSearchDTO.getType() == null){
//            broadcasts = broadcastRepository.findByTimeRange(broadcastSearchDTO.getFromTime(), broadcastSearchDTO.getToTime());
//        } else if (broadcastSearchDTO.getDate() == null) {
//            broadcasts = broadcastRepository.findByTimeRangeType(broadcastSearchDTO.getFromTime(), broadcastSearchDTO.getToTime(), broadcastSearchDTO.getType());
//        } else if (broadcastSearchDTO.getType() == null) {
//            broadcasts = broadcastRepository.findByTimeRangeDate(broadcastSearchDTO.getFromTime(), broadcastSearchDTO.getToTime(), broadcastSearchDTO.getDate());
//        } else {
//            broadcasts = broadcastRepository.findByTimeRangeDateType(broadcastSearchDTO.getFromTime(), broadcastSearchDTO.getToTime(), broadcastSearchDTO.getDate(), broadcastSearchDTO.getType());
//        }
//        return outputBroadcastMapper.toRepresentationList(broadcasts);
//    }

    @Transactional
    public AdBroadcast scheduleAd(Integer id, AdBasicRepresentation ad, LocalTime startingTime){
        Broadcast broadcast = broadcastRepository.findByIdAdDetails(id);
        if (broadcast == null){
            throw new NotFoundException("Broadcast not found");
        }
        int adBroadcastsInTimezone = adBroadcastRepository.findByTimezoneDate(broadcast.getTimezone(), broadcast.getStartingDate()).size();
        System.out.println("adBroadcastsInTimezone - " + adBroadcastsInTimezone);
        // check restriction of maximum 4 ads per timezone
        if (adBroadcastsInTimezone >= 4){
            throw new RadioException("Add cannot be scheduled to broadcast");
        }
        System.out.println ("adId " + ad.id);
        System.out.println ("adId " + ad.timezone);
        AdBroadcast created = broadcast.createAdBroadcast(ad, startingTime);
        System.out.println("created - " + created);
        if (created == null){
            throw new RadioException("Add cannot be scheduled to broadcast");
        }
        adBroadcastRepository.persist(created);
        return created;
    }
//
//    @Transactional
//    public SongBroadcast scheduleSong(Integer id, SongBasicRepresentation song, LocalTime startingTime){
//        Broadcast broadcast = broadcastRepository.findByIdSongDetails(id);
//        if (broadcast == null){
//            throw new NotFoundException("Broadcast not found");x
//        }
//        SongBroadcast created = broadcast.createSongBroadcast(song, startingTime);
//        if (created == null){
//            throw new RadioException("Song cannot be scheduled to broadcast");
//        }
//        songBroadcastRepository.persist(created);
//        return created;
//    }
//
    @Transactional
    public void removeAdBroadcast(Integer id, Integer abId){
        Broadcast broadcast = broadcastRepository.findByIdAdDetails(id);
        if (broadcast == null){
            throw new NotFoundException("Broadcast not found");
        }
        AdBroadcast adBroadcast = adBroadcastRepository.findById(abId);
        // maybe the below check should be removed - to check
        if(adBroadcast == null){
            throw new NotFoundException("Add Broadcast not found");
        }
        broadcast.removeAdBroadcast(adBroadcast);
        adBroadcastRepository.deleteById(abId);
    }
//
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
//
//    @Transactional
//    public SuggestionsDTO suggestions(Integer id){
//        SuggestionsDTO suggestions = new SuggestionsDTO();
//        List<Ad> ads = suggestionsService.suggestAds(id);
//        suggestions.ads = adMapper.toRepresentationList(ads);
//        List<Song> songs = suggestionsService.suggestSongs(id);
//        suggestions.songs = songMapper.toRepresentationList(songs);
//        return suggestions;
//    }
//
//    @Transactional
//    public BroadcastOutputRepresentation getNow(){
//        LocalTime timeNow = DateUtil.timeNow();
//        LocalDate dateNow = DateUtil.dateNow();
//        LocalDateTime dateTimeNow = dateNow.atTime(timeNow);
//        List<Broadcast> broadcastsOfDay = broadcastRepository.findByDate(dateNow);
//        try {
//            broadcastsOfDay = broadcastsOfDay.stream().filter(s -> DateUtil.between(s.getStartingDate().atTime(s.getStartingTime()), dateTimeNow, s.getBroadcastEndingDateTime())).collect(Collectors.toList());
//            return outputBroadcastMapper.toRepresentation(broadcastsOfDay.get(0));
//        }catch (Exception e){
//            return new BroadcastOutputRepresentation();
//        }
//    }

//    private Broadcast updateValues(Broadcast broadcast, BroadcastRepresentation broadcastRepresentation){
//        broadcast.setType(broadcastRepresentation.type);
//        LocalDate date = DateUtil.setDate(broadcastRepresentation.startingDate);
//        LocalTime time = DateUtil.setTime(broadcastRepresentation.startingTime);
//        broadcast.setStartingTime(time);
//        broadcast.setStartingDate(date);
//        broadcast.setDuration(broadcastRepresentation.duration);
//        return broadcast;
//    }
}
