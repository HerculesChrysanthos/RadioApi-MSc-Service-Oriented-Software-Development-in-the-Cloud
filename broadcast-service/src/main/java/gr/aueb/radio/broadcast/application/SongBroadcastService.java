package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.NotFoundRadioException;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
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
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static com.arjuna.ats.jbossatx.logging.jbossatxLogger.logger;
import static io.quarkus.arc.ComponentsProvider.LOG;

@RequestScoped
public class SongBroadcastService {
    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    BroadcastRepository broadcastRepository;

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

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }

        // call content api
        SongBasicRepresentation song = contentService.getSong(auth, dto.songId);
        if (song == null) {
            throw new NotFoundException("Song does not exist");
        }

        // query songbroadcast repo me vash to broadcastId
        // apo to query tha paroume songIds

        Broadcast broadcast = broadcastRepository.findById(dto.broadcastId);
        StringBuilder songsIds = new StringBuilder();
        for (int i = 0; i < broadcast.getSongBroadcasts().size(); i++) {
            int songId = broadcast.getSongBroadcasts().get(i).getSongId();

            songsIds.append(songId);
            if (i != broadcast.getSongBroadcasts().size() - 1) {
                songsIds.append(",");
            }
        }

        List<SongBasicRepresentation> broadcastSongs = contentService.getSongsByFilters(auth, null, null, null, null, songsIds.toString());

        SongBroadcast songBroadcast = broadcastService.scheduleSong(dto.broadcastId, song, DateUtil.setTime(dto.startingTime), broadcastSongs);
        return songBroadcast;
    }

    @Transactional
    @Fallback(fallbackMethod = "findFallback")
    public SongBroadcast find(Integer id, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }

        SongBroadcast songBroadcast = songBroadcastRepository.findByIdDetails(id);
        if (songBroadcast == null) {
            throw new NotFoundException("Song Broadcast does not exist");
        }
        return songBroadcast;
    }

    public SongBroadcast findFallback(Integer id, String auth) {
        LOG.error("An error occurred while executing find method with ID {}: {}");
        SongBroadcast songBroadcast = songBroadcastRepository.findByIdDetails(id);
        if (songBroadcast == null) {
            throw new NotFoundException("Song Broadcast does not exist");
        }
        return songBroadcast;
    }

    @Transactional
    public void delete(Integer id, String auth) {
        //verify auth
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }

        SongBroadcast songBroadcast = songBroadcastRepository.findById(id);
        if (songBroadcast == null) {
            throw new NotFoundException("Song Broadcast does not exist");
        }
        broadcastService.removeSongBroadcast(songBroadcast.getBroadcast().getId(), songBroadcast.getId());
    }

    @Timeout(5000)
    @Transactional
    @Fallback(fallbackMethod = "searchFallback")
    public List<SongBroadcast> search(String date, Integer songId, String auth) {
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }

        try {
            Thread.sleep(6000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LocalDate dateToSearch;
        if (date == null) {
            dateToSearch = null;
        } else {
            dateToSearch = DateUtil.setDate(date);
        }

        return songBroadcastRepository.findByFilters(dateToSearch, songId);
    }

    public List<SongBroadcast> searchFallback(String date, Integer songId, String auth) {

        LOG.error("Fallback method triggered for search operation for search method");

        return songBroadcastRepository.findByFilters(null, 0);
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

    @Transactional
    public void deleteByFilters(String auth, Integer songId) {
        String userRole = userService.verifyAuth(auth).role;

        if (!userRole.equals("PRODUCER")) {
            throw new RadioException("Not Allowed to access this.", 403);
        }
        List<SongBroadcast> sb = songBroadcastRepository.searchBySongId(songId);
        if (sb.size() == 0) {
            throw new NotFoundException("Song Broadcast does not exist");
        }
        songBroadcastRepository.deleteBySongId(songId);
    }

}
