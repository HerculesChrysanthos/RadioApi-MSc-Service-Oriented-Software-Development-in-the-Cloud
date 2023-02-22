package gr.aueb.radio.domains;

import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {
    
    Song song;
    SongBroadcast songBroadcast;
    
    private static LocalDate localDate = DateUtil.setDate("01-01-2023");
    private static LocalTime localTime = DateUtil.setTime("01:01");
    private static String title = "Title";
    private static String genre = "Genre";
    private static int duration = 180;
    private static String artist = "Artist";
    private static Integer year = 2021;

    @BeforeEach
    public void setUp() {
        song = new Song();
        songBroadcast = new SongBroadcast();

        songBroadcast.setBroadcastDate(localDate);
        songBroadcast.setBroadcastTime(localTime);
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
    }

    @Test
    public void addValidSongBroadcastTest() {
        song.addSongBroadcast(songBroadcast);
        List<SongBroadcast> broadcasts = song.getSongBroadcasts();
        assertNotNull(broadcasts);
        assertEquals(1, broadcasts.size());
    }

    @Test
    public void addInvalidSongBroadcastTest() {
        song.addSongBroadcast(null);
        List<SongBroadcast> broadcasts = song.getSongBroadcasts();
        assertEquals(0, broadcasts.size());
    }

    @ParameterizedTest
    @CsvSource({
            "Title, Genre, 180, Artist, 2021, true",
            "Title, Genre, 180, Artist, 2021, true",
            "Title, Genre, 180, Artist, 2021, true",
            "Title, Genre, 180, Artist, 2021, true",
    })
    public void equalsSongDataTest(String title, String genre, Integer duration, String artist, Integer year, boolean expectedResult){
        Song song1 = new Song(title, genre, duration, artist, year);
        assertEquals(expectedResult, song.equals(song1));
    }

    @Test
    public void equalsObjectTest(){
        Assertions.assertTrue(song.equals(song));
        assertFalse(song.equals(null));
        assertFalse(song.equals(String.class));
    }
}
