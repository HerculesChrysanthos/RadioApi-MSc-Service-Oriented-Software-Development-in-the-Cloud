package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.NotFoundException;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.SongBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.SongBroadcastCreationDTO;
import gr.aueb.radio.broadcast.application.UserService;
import gr.aueb.radio.broadcast.common.RadioException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequestScoped
public class SongBroadcastService {
    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    BroadcastService broadcastService;

    @Inject
    UserService userService;

//    @Inject
//    SongRepository songRepository;

//    @Transactional
//    public SongBroadcast create(SongBroadcastCreationDTO dto) {
//        //Song song = songRepository.findById(dto.songId);
////        if (song == null){
////            throw new NotFoundException("Song does not exist");
////        }
//        SongBroadcast songBroadcast = broadcastService.scheduleSong(dto.broadcastId, song, DateUtil.setTime(dto.startingTime));
//        return songBroadcast;
//    }

    @Transactional
    public SongBroadcast find(Integer id) {
        SongBroadcast songBroadcast = songBroadcastRepository.findByIdDetails(id);
        if (songBroadcast == null){
            throw new NotFoundException("Song Broadcast does not exist");
        }
        return songBroadcast;
    }

//    @Transactional
//    public void delete(Integer id) {
//        SongBroadcast songBroadcast = songBroadcastRepository.findById(id);
//        if (songBroadcast == null){
//            throw new NotFoundException("Song Broadcast does not exist");
//        }
//        broadcastService.removeSongBroadcast(songBroadcast.getBroadcast().getId(), songBroadcast.getId());
//    }


    @Transactional
    public List<SongBroadcast> search(String date, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to access this.", 403);
        }

        LocalDate dateToSearch;
        if (date == null){
            dateToSearch = DateUtil.dateNow();
        }else{
            dateToSearch = DateUtil.setDate(date);
        }
        return songBroadcastRepository.findByDateDetails(dateToSearch);
    }
}
