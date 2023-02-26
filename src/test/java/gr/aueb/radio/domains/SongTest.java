package gr.aueb.radio.domains;

import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {
    
    Song song;
    SongBroadcast songBroadcast;
    
    private static LocalDate localDate = DateUtil.setDate("01-01-2023");
    private static LocalTime localTime = DateUtil.setTime("01:01");
    private static String title = "Title";
    private static String genre = "Genre";
    private static int duration = 10;
    private static String artist = "Artist";
    private static Integer year = 2021;

    @BeforeEach
    public void setUp() {
        song = new Song();
        songBroadcast = new SongBroadcast(localDate, localTime);

        song.setTitle(title);
        song.setGenre(genre);
        song.setDuration(duration);
        song.setArtist(artist);
        song.setYear(year);
    }

    @Test
    public void ValidSong() throws Exception {
        Song song = new Song("Title", "Genre", 180, "Artist", 2021);
        assertEquals(song.getArtist(),("Artist"));
    }
    @Test
    public void successfulSetUp() {
        assertEquals(song.getTitle(), title);
        assertEquals(song.getGenre(), genre);
        assertEquals(song.getArtist(), artist);
        assertEquals(song.getYear(), year);
        assertEquals(song.getDuration(), duration);
    }

    @Test
    public void addValidSongBroadcastTest() {
        song.addSongBroadcast(songBroadcast);
        List<SongBroadcast> broadcasts = song.getSongBroadcasts();
        assertNotNull(broadcasts);
        assertEquals(1, broadcasts.size());
    }

    @Test
    public void removeSongBroadcast(){
        song.addSongBroadcast(songBroadcast);
        List<SongBroadcast> broadcasts = song.getSongBroadcasts();
        assertEquals(1, broadcasts.size());
        song.removeSongBroadcast(songBroadcast);
        broadcasts = song.getSongBroadcasts();
        assertEquals(0, broadcasts.size());
    }

    @Test
    public void addInvalidSongBroadcastTest() {
        song.addSongBroadcast(null);
        List<SongBroadcast> broadcasts = song.getSongBroadcasts();
        assertEquals(0, broadcasts.size());
    }

    @Test
    public void setTitleTest() {
        String newTitle = "New Title";
        song.setTitle(newTitle);
        assertEquals(newTitle, song.getTitle());
   }

   @Test
   public void setGenreTest() {
        String newGenre = "New Genre";
        song.setGenre(newGenre);
        assertEquals(newGenre, song.getGenre());
   }

    @Test
    public void setArtistTest() {
        String newArtist = "New Artist";
        song.setArtist(newArtist);
        assertEquals(newArtist, song.getArtist());
    }

    @Test
    public void setYearTest() {
        Integer newYear = 2022;
        song.setYear(newYear);
        assertEquals(newYear, song.getYear());
    }

    @Test
    public void setDurationTest() {
        int newDuration = 240;
        song.setDuration(newDuration);
        assertEquals(newDuration, song.getDuration());
    }

    @Test
    public void equalsTest() {
        Song song2 = new Song("Title", "Genre", 300, "Artist", 2021);
        assertFalse(song.equals(song2));
    }

    @Test
    public void rateConstraintsTest(){
        SongBroadcast broadcast1 = new SongBroadcast(DateUtil.setDate("01-01-2023"), DateUtil.setTime("01:00"));
        SongBroadcast broadcast2 = new SongBroadcast(DateUtil.setDate("01-01-2023"), DateUtil.setTime("02:30"));
        SongBroadcast broadcast3 = new SongBroadcast(DateUtil.setDate("01-01-2023"), DateUtil.setTime("04:00"));
        song.addSongBroadcast(broadcast1);
        song.addSongBroadcast(broadcast2);
        song.addSongBroadcast(broadcast3);
        assertTrue(song.toBeBroadcasted(DateUtil.setDate("01-01-2023"), DateUtil.setTime("05:30")));

        SongBroadcast broadcast4 = new SongBroadcast(DateUtil.setDate("01-01-2023"), DateUtil.setTime("05:30"));
        song.addSongBroadcast(broadcast4);
        assertEquals(4, song.getSongBroadcasts().size());
        assertFalse(song.toBeBroadcasted(DateUtil.setDate("01-01-2023"), DateUtil.setTime("08:00")));
    }

    @Test
    public void timeConstraintsTest(){
        SongBroadcast broadcast1 = new SongBroadcast(DateUtil.setDate("01-01-2023"), DateUtil.setTime("10:00"));
        SongBroadcast broadcast2 = new SongBroadcast(DateUtil.setDate("01-01-2023"), DateUtil.setTime("12:00"));
        song.addSongBroadcast(broadcast1);
        song.addSongBroadcast(broadcast2);
        assertTrue(song.toBeBroadcasted(DateUtil.setDate("01-01-2023"), DateUtil.setTime("05:30")));
        assertTrue(song.toBeBroadcasted(DateUtil.setDate("01-01-2023"), DateUtil.setTime("15:30")));
        assertFalse(song.toBeBroadcasted(DateUtil.setDate("01-01-2023"), DateUtil.setTime("11:00")));
        assertFalse(song.toBeBroadcasted(DateUtil.setDate("01-01-2023"), DateUtil.setTime("11:30")));
        assertTrue(song.toBeBroadcasted(DateUtil.setDate("01-02-2023"), DateUtil.setTime("05:30")));
    }

}

   