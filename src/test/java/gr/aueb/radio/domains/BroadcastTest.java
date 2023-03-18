package gr.aueb.radio.domains;

import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BroadcastTest {



    Broadcast broadcast;
    private static LocalDate date = DateUtil.setDate("01-01-2023");
    private static LocalTime time = DateUtil.setTime("00:00");
    private static Integer duration = 100;
    @BeforeEach
    public void setUp(){
        broadcast = new Broadcast();
        broadcast.setDuration(duration);
        broadcast.setStartingDate(date);
        broadcast.setStartingTime(time);
        broadcast.setType(BroadcastEnum.PLAYLIST);

    }

    @Test
    public void successfulSetupTest(){
        assertEquals(duration, broadcast.getDuration());
        assertEquals(date, broadcast.getStartingDate());
        assertEquals(time, broadcast.getStartingTime());
        assertEquals(BroadcastEnum.PLAYLIST, broadcast.getType());
    }
    
    @Test
    public void createValidSongBroadcastTest() {
        Song song = new Song("Title", "Genre", 10, "Artist", 2021);
        broadcast.createSongBroadcast(song, DateUtil.setTime("00:00"));
        List<SongBroadcast> broadcasts = broadcast.getSongBroadcasts();
        assertNotNull(broadcasts);
    }

    @Test
    public void removeValidSongBroadcastTest() {
        Song song = new Song("Title", "Genre", 10, "Artist", 2021);
        SongBroadcast songBroadcast = broadcast.createSongBroadcast(song, DateUtil.setTime("00:00"));
        assertNotNull(songBroadcast);
        List<SongBroadcast> broadcasts = broadcast.getSongBroadcasts();
        assertEquals(1, broadcasts.size());
        songBroadcast = broadcasts.get(0);
        broadcast.removeSongBroadcast(songBroadcast);
        assertEquals(0, broadcasts.size());
    }

    @Test
    public void createValidAdBroadcastTest() {
        Ad ad = new Ad(5, 1, DateUtil.setDate("01-01-2023"), DateUtil.setDate("01-05-2023"), ZoneEnum.LateNight);
        broadcast.createAdBroadcast(ad, DateUtil.setTime("00:00"));
        List<AdBroadcast> broadcasts = broadcast.getAdBroadcasts();
        assertNotNull(broadcasts);
        assertEquals(1, broadcasts.size());
    }

    @Test
    public void removeValidAdBroadcastTest() {
        Ad ad = new Ad(5, 1, DateUtil.setDate("01-01-2023"), DateUtil.setDate("01-05-2023"), ZoneEnum.LateNight);
        broadcast.createAdBroadcast(ad, DateUtil.setTime("01:00"));
        List<AdBroadcast> broadcasts = broadcast.getAdBroadcasts();
        assertEquals(1, broadcasts.size());
        AdBroadcast adBroadcast = broadcasts.get(0);
        broadcast.removeAdBroadcast(adBroadcast);
        assertEquals(0, broadcasts.size());
    }

    @Test
    public void getEndingTimeTest(){
        LocalDateTime expectedTime = date.atTime(time).plusMinutes(duration);
        LocalDateTime endingTime = broadcast.getBroadcastEndingDateTime();
        assertTrue(expectedTime.isEqual(endingTime));
    }
}
