package gr.aueb.radio.services;

import gr.aueb.radio.IntegrationBaseExtended;
import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.dto.SongBroadcastCreationDTO;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.persistence.SongBroadcastRepository;
import gr.aueb.radio.persistence.SongRepository;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest

public class SongBroadcastServiceTest extends IntegrationBaseExtended{
    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    SongBroadcastService songBroadcastService;

    @Inject
    SongRepository songRepository;

    @Test
    @TestTransaction
    public void CreateAdBroadcastTest() {

        List<SongBroadcast> songBroadcastListBroadcastsList = songBroadcastRepository.listAll();
        int numOfSongBroadcasts = songBroadcastListBroadcastsList.size();

        SongBroadcastCreationDTO sbNull = new SongBroadcastCreationDTO(3002,-1,"08:00");
        assertThrows(NotFoundException.class, () -> {
            songBroadcastService.create(sbNull);
        });

        SongBroadcastCreationDTO sb = new SongBroadcastCreationDTO(3002,7597,"08:00");

        SongBroadcast created = songBroadcastService.create(sb);
        assertEquals(numOfSongBroadcasts + 1, songBroadcastRepository.listAll().size());
        // check correct creation ex. starting date
        LocalTime timeToCheck = DateUtil.setTime(sb.startingTime);
        assertTrue(timeToCheck.equals(created.getBroadcastTime()));

    }

    @Test
    @TestTransaction
    public void findAdTest(){
        List<SongBroadcast> songBroadcastList = songBroadcastRepository.listAll();
        Integer songBroadcastId = songBroadcastList.get(0).getId();
        SongBroadcast sb = songBroadcastService.find(songBroadcastId);
        assertNotNull(sb);
        assertThrows(NotFoundException.class, ()->songBroadcastService.find(-1));
    }

    @Test
    @TestTransaction
    public void findAdBrDateTest(){
        // find Add broadcast in db
        SongBroadcast existing = songBroadcastRepository.listAll().get(0);
        LocalDate dateToSearch = existing.getBroadcastDate();
        List<SongBroadcast> songBroadcastList = songBroadcastService.search(DateUtil.setDateToString(dateToSearch));
        for(SongBroadcast songBroadcast : songBroadcastList) {
            assertEquals(songBroadcast.getBroadcastDate(), dateToSearch);
        }
        songBroadcastList = songBroadcastService.search(null);
        assertEquals(0, songBroadcastList.size());
    }

    @Test
    @TestTransaction
    public void DeleteAdBrTest() {

        List<SongBroadcast> songBroadcastList = songBroadcastRepository.listAll();
        Integer songBroadcastId = songBroadcastList.get(0).getId();
        int numOfSongBroadcasts = songBroadcastList.size();

        // adId does to be updated not found
        assertThrows(NotFoundException.class, () -> {
            songBroadcastService.delete(-1);
        });

        songBroadcastService.delete(songBroadcastId);
        assertNotEquals(numOfSongBroadcasts, songRepository.listAll().size());

    }

}
