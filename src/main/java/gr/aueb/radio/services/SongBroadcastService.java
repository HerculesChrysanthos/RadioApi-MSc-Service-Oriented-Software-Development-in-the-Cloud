package gr.aueb.radio.services;

import gr.aueb.radio.domains.Song;
import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.dto.SongBroadcastCreationDTO;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.mappers.SongBroadcastMapper;
import gr.aueb.radio.persistence.SongBroadcastRepository;
import gr.aueb.radio.persistence.SongRepository;
import gr.aueb.radio.utils.DateUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RequestScoped
public class SongBroadcastService {
    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    BroadcastService broadcastService;

    @Inject
    SongRepository songRepository;

    @Transactional
    public SongBroadcast create(SongBroadcastCreationDTO dto) {
        Song song = songRepository.findById(dto.songId);
        if (song == null){
            throw new NotFoundException("Song does not exist");
        }
        SongBroadcast songBroadcast = broadcastService.scheduleSong(dto.broadcastId, song, DateUtil.setTime(dto.startingTime));
        return songBroadcast;
    }

    @Transactional
    public SongBroadcast find(Integer id) {
        SongBroadcast songBroadcast = songBroadcastRepository.findByIdDetails(id);
        if (songBroadcast == null){
            throw new NotFoundException("Song Broadcast does not exist");
        }
        return songBroadcast;
    }

    @Transactional
    public void delete(Integer id) {
        SongBroadcast songBroadcast = songBroadcastRepository.findById(id);
        if (songBroadcast == null){
            throw new NotFoundException("Song Broadcast does not exist");
        }
        broadcastService.removeSongBroadcast(songBroadcast.getBroadcast().getId(), songBroadcast.getId());
    }


    @Transactional
    public List<SongBroadcast> search(String date) {
        LocalDate dateToSearch;
        if (date == null){
            dateToSearch = DateUtil.dateNow();
        }else{
            dateToSearch = DateUtil.setDate(date);
        }
        return songBroadcastRepository.findByDateDetails(dateToSearch);
    }
}
