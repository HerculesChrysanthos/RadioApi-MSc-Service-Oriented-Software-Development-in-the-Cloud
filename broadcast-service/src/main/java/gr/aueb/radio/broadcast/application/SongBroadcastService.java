package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.NotFoundRadioException;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.SongBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.SongBroadcastCreationDTO;
import gr.aueb.radio.broadcast.application.UserService;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequestScoped
public class SongBroadcastService {
    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    BroadcastService broadcastService;

    @Inject
    UserService userService;

    @Inject
    ContentService contentService;

//    @Inject
//    SongRepository songRepository;

    @Transactional
    public SongBroadcast create(SongBroadcastCreationDTO dto, String auth) {
        // verify auth
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to access this.", 403);
        }

        // call content api
        SongBasicRepresentation song = contentService.getSong(auth, dto.songId);
        if (song == null){
            throw new NotFoundException("Ad does not exist");
        }
        //Song song = songRepository.findById(dto.songId);
//        if (song == null){
//            throw new NotFoundException("Song does not exist");
//        }
        //songbroadcastService.scheduleSong move to
        SongBroadcast songBroadcast = broadcastService.scheduleSong(dto.broadcastId, song, DateUtil.setTime(dto.startingTime));
//        SongBroadcast songBroadcast = scheduleSong(dto.broadcastId, song, DateUtil.setTime(dto.startingTime));
        return songBroadcast;
    }

    @Transactional
    public SongBroadcast find(Integer id, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to access this.", 403);
        }

        SongBroadcast songBroadcast = songBroadcastRepository.findByIdDetails(id);
        if (songBroadcast == null){
            throw new RadioException("Song Broadcast does not exist");
        }
        return songBroadcast;
    }

    @Transactional
    public void delete(Integer id, String auth) {
        //verify auth
        String userRole = userService.verifyAuth(auth).role;

        if(!userRole.equals("PRODUCER")){
            throw new RadioException("Not Allowed to access this.", 403);
        }

        SongBroadcast songBroadcast = songBroadcastRepository.findById(id);
        if (songBroadcast == null){
            throw new NotFoundException("Song Broadcast does not exist");
        }
        broadcastService.removeSongBroadcast(songBroadcast.getBroadcast().getId(), songBroadcast.getId());
    }


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



//    @Transactional
//    public List<SongBroadcast> searchBySongId (String date, String auth) {
//        String userRole = userService.verifyAuth(auth).role;
//
//        if(!userRole.equals("PRODUCER")){
//            throw new RadioException("Not Allowed to access this.", 403);
//        }
//        return songBroadcastRepository.searchBySongId(songId);
//    }

}
