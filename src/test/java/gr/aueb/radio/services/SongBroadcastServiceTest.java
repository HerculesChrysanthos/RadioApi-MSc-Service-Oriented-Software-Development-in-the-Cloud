package gr.aueb.radio.services;

import gr.aueb.radio.IntegrationBaseExtended;
import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.dto.SongBroadcastCreationDTO;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.persistence.SongBroadcastRepository;
import gr.aueb.radio.persistence.SongRepository;
import gr.aueb.radio.representations.SongRepresentation;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest

public class SongBroadcastServiceTest {
    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    SongBroadcastService songBroadcastService;

    @Inject
    SongRepository songRepository;

    @Test
    @TestTransaction
    public void CreateAdBroadcastTest() {

        List<SongBroadcast> songBroadcastsList = songBroadcastRepository.listAll();
      //    Integer sbBroadcastId = sdBroadcastsList.get(0).getId();
        int numOfSongBroadcasts = songBroadcastsList.size();

        SongBroadcastCreationDTO sb = new SongBroadcastCreationDTO(2542,135,"12:00");

        SongBroadcast created = songBroadcastService.create(sb);;
        assertEquals(numOfSongBroadcasts + 1, songBroadcastRepository.listAll().size());
       
        LocalTime timeToCheck = DateUtil.setTime(sb.startingTime);
        assertTrue(timeToCheck.equals(created.getBroadcastTime()));

    }

    @Transactional
    public void testCreateSongBroadcastWithNonExistingSong() {
        SongBroadcastCreationDTO dto = new SongBroadcastCreationDTO(1, 9999, "10:00");
        assertThrows(NotFoundException.class, () -> songBroadcastService.create(dto));
    }

    @Test
    @TestTransaction
    public void findAdTest(){
        List<SongBroadcast> songBroadcastsList = songBroadcastRepository.listAll();
        Integer songBroadcastId = songBroadcastsList.get(0).getId();
        SongBroadcast songbroadcast = songBroadcastService.find(songBroadcastId);
        assertNotNull(songBroadcastId);
    
    }

}
