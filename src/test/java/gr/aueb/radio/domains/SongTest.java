package gr.aueb.radio.domains;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;

public class SongTest {
    
    Song song;
    Transmission transmission;
    
    private static LocalDate localDate = LocalDate.of(2023, Month.FEBRUARY, 1);
    private static LocalTime localTime = LocalTime.of(1, 0);
    private static String Title = "Title";
    private static String Genre = "Genre";
    private static int Duration = 180;
    private static String Artist = "Artist";
    private static int Year = 2021;


    @Test
    public void ValidSong() throws Exception {
        Song song = new Song("Title", "Genre", 180, "Artist", 2021);
        assertEquals(song.getArtist(),("Artist"));
    }

    @BeforeEach
    public void setUp() {
        song = new Song();
        transmission = new Transmission();

        transmission.setDate(localDate);
        transmission.setTime(localTime);
        song.setTitle(Title);
        song.setGenre(Genre);
        song.setDuration(Duration);
        song.setArtist(Artist);
        song.setYear(Year);
    }
    
    @Test
    public void succefulSetUp() {
        assertEquals(song.getTitle(), Title);
        assertEquals(song.getGenre(), Genre);
        assertEquals(song.getDuration(), Duration);
        assertEquals(song.getArtist(), Artist);
        assertEquals(song.getYear(), Year);
    }

    @Test
    public void addTransmission() {        
        song.setTransmission(transmission);
        assertEquals(song.getTransmission(), transmission);
    }

    @Test
    public void testValidYear() {
        song = new Song("Title", "Genre", 160, "Artist", 2000);
        assertTrue(song.getYear() >=1900 && song.getYear() <= 2030);
    }

    @Test
    public void testInvalidTitle() {
        String invalidTitle = "";
        song.setTitle(invalidTitle);
        assertEquals(song.getTitle(), "");
    }

    @Test
    public void testInvalidGenre() {
        String invalidGenre = null;
        song.setGenre(invalidGenre);
        assertEquals(song.getGenre(), null);
    }
}
