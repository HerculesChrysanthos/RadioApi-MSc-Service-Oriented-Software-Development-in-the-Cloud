package gr.aueb.radio.services;

import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.domains.*;
import gr.aueb.radio.dto.BroadcastSearchDTO;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.BroadcastMapper;
import gr.aueb.radio.persistence.*;
import gr.aueb.radio.representations.BroadcastOutputRepresentation;
import gr.aueb.radio.representations.BroadcastRepresentation;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.inject.Inject;
import javax.transaction.Transactional;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
public class BroadcastServiceTest extends IntegrationBase {
    @Inject
    BroadcastService broadcastService;

    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    AdRepository adRepository;

    @Inject
    SongRepository songRepository;

    @Inject
    BroadcastMapper broadcastMapper;

    private BroadcastRepresentation createRepresentation(){
        BroadcastRepresentation broadcastRepresentation = new BroadcastRepresentation();
        broadcastRepresentation.duration = 45;
        broadcastRepresentation.startingDate = "01-02-2022";
        broadcastRepresentation.startingTime = "01:00";
        broadcastRepresentation.type = BroadcastEnum.NEWS;
        return broadcastRepresentation;
    }

    @Test
    @TestTransaction
    public void findTest(){
        Integer validId = broadcastRepository.listAll().get(0).getId();
        assertThrows(NotFoundException.class, () -> {
           broadcastService.findById(0);
        });
        Broadcast broadcast = broadcastService.findById(validId);
        assertNotNull(broadcast);
    }

    @Test
    @TestTransaction
    public void checkOverlappingTest(){
        Broadcast broadcast = new Broadcast();
        broadcast.setStartingDate(DateUtil.setDate("01-02-2022"));
        broadcast.setType(BroadcastEnum.PLAYLIST);
        broadcast.setDuration(100);
        broadcast.setStartingTime(DateUtil.setTime("01:00"));
        boolean result = broadcastService.checkForOverlapping(
                broadcast.getStartingDate(),
                broadcast.getStartingTime(),
                broadcast.getStartingTime().plusMinutes(broadcast.getDuration()),
                -1
        );
        assertTrue(result);
        broadcast.setStartingTime(DateUtil.setTime("23:00"));
        result = broadcastService.checkForOverlapping(
                broadcast.getStartingDate(),
                broadcast.getStartingTime(),
                broadcast.getStartingTime().plusMinutes(broadcast.getDuration()),
                -1
        );
        assertTrue(result);
        broadcast.setStartingTime(DateUtil.setTime("12:00"));
        result = broadcastService.checkForOverlapping(
                broadcast.getStartingDate(),
                broadcast.getStartingTime(),
                broadcast.getStartingTime().plusMinutes(broadcast.getDuration()),
                -1
        );
        assertFalse(result);
    }

    @Test
    @TestTransaction
    public void createTest(){
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        assertThrows(RadioException.class, () -> {
            broadcastService.create(broadcastRepresentation);
        });
        broadcastRepresentation.startingDate = "01-04-2022";
        broadcastRepresentation.startingTime = "20:00";
        int size = broadcastRepository.listAll().size();
        Broadcast created = broadcastService.create(broadcastRepresentation);
        assertEquals(size + 1, broadcastRepository.listAll().size());
        assertNotNull(broadcastRepository.findById(created.getId()));
    }

    @Test
    public void updateTest(){
        Broadcast broadcastFound = broadcastRepository.listAll().get(0);
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        // Trigger exception because broadcast with id does not exist
        assertThrows(NotFoundException.class, () -> {
            broadcastService.update(-1, broadcastRepresentation);
        });
        // Broadcast is immutable, it has song/add scheduled
        assertThrows(RadioException.class, () -> {
            broadcastService.update(broadcastFound.getId(), broadcastRepresentation);
        });
        broadcastRepresentation.startingTime = "21:00";
        Broadcast created = broadcastService.create(broadcastRepresentation);
        broadcastRepresentation.startingTime = "01:00";
        // Trigger overlapping exception
        assertThrows(RadioException.class, () -> {
            broadcastService.update(created.getId(), broadcastRepresentation);
        });
        broadcastRepresentation.startingTime = "12:00";
        // get list of broadcasts before update to check that the broadcast is updated and no other broadcast is created
        int numOfBroadcasts = broadcastRepository.listAll().size();
        Broadcast updated = broadcastService.update(created.getId(),broadcastRepresentation);
        assertEquals(numOfBroadcasts, broadcastRepository.listAll().size());
        assertEquals(created.getId(), updated.getId());
        assertEquals(broadcastRepresentation.duration, updated.getDuration());
        LocalDate dateToCheck = DateUtil.setDate(broadcastRepresentation.startingDate);
        assertTrue(dateToCheck.equals(updated.getStartingDate()));
        LocalTime timeToCheck = DateUtil.setTime(broadcastRepresentation.startingTime);
        assertTrue(timeToCheck.equals(updated.getStartingTime()));
        assertEquals(broadcastRepresentation.type, updated.getType());
    }

    @Test
    @Transactional
    public void deleteTest(){
        int initialNumAddBroadcasts = adBroadcastRepository.listAll().size();
        int initialNumSongBroadcasts = songBroadcastRepository.listAll().size();
        int initialNumBroadcasts = broadcastRepository.listAll().size();
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        broadcastRepresentation.startingDate = "12-12-2012";
        broadcastRepresentation.startingTime = "13:00";
        // Create a new broadcast
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);
        assertNotNull(broadcast);
        assertEquals(initialNumBroadcasts + 1, broadcastRepository.listAll().size());

        // create a new add and schedule for created broadcast
        Ad validAd = new Ad(2, 4, DateUtil.setDate("01-12-2012"), DateUtil.setDate("22-12-2012"), ZoneEnum.Noon);
        adRepository.persist(validAd);
        AdBroadcast adBroadcast = broadcastService.scheduleAd(broadcast.getId(), validAd, DateUtil.setTime("13:25"));
        assertNotNull(adBroadcast);
        assertEquals(1, broadcast.getAdBroadcasts().size());
        assertEquals(1, validAd.getBroadcastAds().size());
        assertEquals(initialNumAddBroadcasts + 1, adBroadcastRepository.listAll().size());

        // create a new song and schedule for created broadcast
        Song validSong = new Song("wtp", "rap", 4, "eminem", 2010);
        songRepository.persist(validSong);
        SongBroadcast songBroadcast = broadcastService.scheduleSong(broadcast.getId(), validSong, DateUtil.setTime("13:00"));
        assertNotNull(songBroadcast);
        assertEquals(1, broadcast.getSongBroadcasts().size());
        assertEquals(1, validSong.getSongBroadcasts().size());
        assertEquals(initialNumSongBroadcasts + 1, songBroadcastRepository.listAll().size());

        assertThrows(NotFoundException.class, ()->{
            broadcastService.delete(-1);
        });

        broadcastService.delete(broadcast.getId());
        assertEquals(0, validAd.getBroadcastAds().size());
        assertEquals(0, validSong.getSongBroadcasts().size());
        assertEquals(initialNumAddBroadcasts, adBroadcastRepository.listAll().size());
        assertEquals(initialNumSongBroadcasts, songBroadcastRepository.listAll().size());
        assertEquals(initialNumBroadcasts, broadcastRepository.listAll().size());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "12:00, 14:00, null, null, 1",
            "12:00, 14:00, 12-12-2012, null, 1",
            "12:00, 14:00, null, NEWS, 1",
            "12:00, 14:00, 12-12-2012, NEWS, 1",
            "12:00, 14:00, 12-12-2013, PLAYLIST, 0",
            "null, null, 12-12-2001, PLAYLIST, 0",
            "null, null, 12-12-2012, NEWS, 1",
    }, nullValues = {"null"})
    public void searchTest(String fromTime, String toTime, String date, BroadcastEnum type, int expectedValue){
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        broadcastRepresentation.startingDate = "12-12-2012";
        broadcastRepresentation.startingTime = "13:00";
        broadcastService.create(broadcastRepresentation);
        BroadcastSearchDTO dto = new BroadcastSearchDTO(fromTime, toTime, date, type);
        List<BroadcastOutputRepresentation> representationsList = broadcastService.search(dto);
        assertEquals(expectedValue, representationsList.size());
    }

    @Test
    @Transactional
    public void scheduleAddTest(){
        int initialNumAdBroadcasts = adBroadcastRepository.listAll().size();
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        broadcastRepresentation.startingDate = "12-12-2012";
        broadcastRepresentation.startingTime = "13:00";
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);

        Ad invalidAd = new Ad(2, 0, DateUtil.setDate("01-12-2012"), DateUtil.setDate("22-12-2012"), ZoneEnum.Noon);
        Ad validAd = new Ad(2, 4, DateUtil.setDate("01-12-2012"), DateUtil.setDate("22-12-2012"), ZoneEnum.Noon);
        adRepository.persist(validAd);
        assertThrows(NotFoundException.class, ()-> {
            broadcastService.scheduleAd(-1, validAd, DateUtil.setTime("13:25"));
        });
        assertThrows(RadioException.class, ()->{
            broadcastService.scheduleAd(broadcast.getId(), invalidAd, DateUtil.setTime("13:25"));
        });
        AdBroadcast adBroadcast = broadcastService.scheduleAd(broadcast.getId(), validAd, DateUtil.setTime("13:25"));
        assertNotNull(adBroadcast);
        assertEquals(1, broadcast.getAdBroadcasts().size());
        assertEquals(1, validAd.getBroadcastAds().size());
        assertEquals(initialNumAdBroadcasts + 1, adBroadcastRepository.listAll().size());
    }

    @Test
    @Transactional
    public void scheduleSongTest(){
        int initialNumSongBroadcasts = songBroadcastRepository.listAll().size();
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        broadcastRepresentation.startingDate = "12-12-2012";
        broadcastRepresentation.startingTime = "13:00";
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);

        Song invalidSong = new Song("title", "whatever genre", 12, "artist", 2022);
        Song validSong = new Song("wtp", "rap", 4, "eminem", 2010);

        songRepository.persist(validSong);

        assertThrows(NotFoundException.class, ()-> {
            broadcastService.scheduleSong(-1, validSong, DateUtil.setTime("13:25"));
        });

        SongBroadcast songBroadcast = broadcastService.scheduleSong(broadcast.getId(), validSong, DateUtil.setTime("13:25"));
        assertNotNull(songBroadcast);
        assertEquals(1, broadcast.getSongBroadcasts().size());

        assertThrows(RadioException.class, ()->{
            broadcastService.scheduleSong(broadcast.getId(), invalidSong, DateUtil.setTime("13:35"));
        });

        assertEquals(1, broadcast.getSongBroadcasts().size());
        assertEquals(1, validSong.getSongBroadcasts().size());
        assertEquals(initialNumSongBroadcasts + 1, songBroadcastRepository.listAll().size());
    }

    @Test
    @Transactional
    public void removeAddBroadcastTest(){
        int initialNumAdBroadcasts = adBroadcastRepository.listAll().size();
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        broadcastRepresentation.startingDate = "12-12-2012";
        broadcastRepresentation.startingTime = "13:00";
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);

        Ad validAd = new Ad(2, 4, DateUtil.setDate("01-12-2012"), DateUtil.setDate("22-12-2012"), ZoneEnum.Noon);
        adRepository.persist(validAd);
        AdBroadcast adBroadcast = broadcastService.scheduleAd(broadcast.getId(), validAd, DateUtil.setTime("13:25"));
        assertNotNull(adBroadcast);
        assertEquals(1, broadcast.getAdBroadcasts().size());
        assertEquals(1, validAd.getBroadcastAds().size());
        assertEquals(initialNumAdBroadcasts + 1, adBroadcastRepository.listAll().size());

        assertThrows(NotFoundException.class, ()->{
            broadcastService.removeAdBroadcast(-1, adBroadcast.getId());
        });

        assertThrows(NotFoundException.class, ()->{
            broadcastService.removeAdBroadcast(broadcast.getId(), -1);
        });

        broadcastService.removeAdBroadcast(broadcast.getId(), adBroadcast.getId());
        assertEquals(0, validAd.getBroadcastAds().size());
        assertEquals(0, broadcast.getAdBroadcasts().size());
        assertEquals(initialNumAdBroadcasts, adBroadcastRepository.listAll().size());
    }

    @Test
    @Transactional
    public void removeSongBroadcastTest(){
        int initialNumSongBroadcasts = songBroadcastRepository.listAll().size();
        BroadcastRepresentation broadcastRepresentation = createRepresentation();
        broadcastRepresentation.startingDate = "12-12-2012";
        broadcastRepresentation.startingTime = "13:00";
        Broadcast broadcast = broadcastService.create(broadcastRepresentation);
        Song validSong = new Song("wtp", "rap", 4, "eminem", 2010);

        songRepository.persist(validSong);

        SongBroadcast songBroadcast = broadcastService.scheduleSong(broadcast.getId(), validSong, DateUtil.setTime("13:25"));
        assertNotNull(songBroadcast);
        assertEquals(1, broadcast.getSongBroadcasts().size());
        assertEquals(1, validSong.getSongBroadcasts().size());
        assertEquals(initialNumSongBroadcasts + 1, songBroadcastRepository.listAll().size());

        assertThrows(NotFoundException.class, ()->{
            broadcastService.removeSongBroadcast(-1, songBroadcast.getId());
        });

        assertThrows(NotFoundException.class, ()->{
            broadcastService.removeSongBroadcast(broadcast.getId(), -1);
        });

        broadcastService.removeSongBroadcast(broadcast.getId(), songBroadcast.getId());
        assertEquals(0, validSong.getSongBroadcasts().size());
        assertEquals(0, broadcast.getSongBroadcasts().size());
        assertEquals(initialNumSongBroadcasts, songBroadcastRepository.listAll().size());
    }


}
