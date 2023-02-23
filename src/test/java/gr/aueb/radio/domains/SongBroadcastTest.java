package gr.aueb.radio.domains;

import gr.aueb.radio.enums.BroadcastEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;


public class SongBroadcastTest {
    
    SongBroadcast songBroadcast;
    
    private static LocalDate localDate = LocalDate.of(2023, Month.FEBRUARY, 1);
    private static LocalTime localTime = LocalTime.of(1, 0);

    private static String title = "Title";
    private static String genre = "Genre";
    private static int duration = 180;
    private static String artist = "Artist";
    private static Integer year = 2021;

    @BeforeEach
    public void setUp() {
        songBroadcast = new SongBroadcast();
        songBroadcast.setBroadcastDate(localDate);
        songBroadcast.setBroadcastTime(localTime);
    }
    
    @Test
    public void successfulSetUp() {
        assertEquals(songBroadcast.getBroadcastDate(), localDate);
        assertEquals(songBroadcast.getBroadcastTime(), localTime);
    }

    @Test
    public void addSongTest(){
        Song song = new Song(title, genre, duration, artist, year);
        songBroadcast.setSong(song);
        assertNotNull(songBroadcast.getSong());
    }

    @Test
    public void addBroadcastTest(){
        Broadcast broadcast = new Broadcast(300, localDate, localTime, BroadcastEnum.NEWS);
        songBroadcast.setBroadcast(broadcast);
        assertNotNull(songBroadcast.getBroadcast());
    }

    @Test
    public void getEndingTimeTest(){
        Song song = new Song(title, genre, duration, artist, year);
        songBroadcast.setSong(song);
        LocalDateTime expectedTime = localDate.atTime(localTime).plusMinutes(duration);
        LocalDateTime endingTime = songBroadcast.getBroadcastEndingDateTime();
        assertTrue(expectedTime.isEqual(endingTime));
    }
}
