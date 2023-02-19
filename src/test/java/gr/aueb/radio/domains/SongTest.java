package gr.aueb.radio.domains;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;

public class SongTest {
    
    Song song;
    Transmission transmission;
    
    private static String Title = "Title";
    private static String Genre = "Genre";
    private static int Duration = 180;
    private static String Artist = "Artist";
    private static int Year = 2021;
    private static LocalDateTime localDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 1, 1, 1, 0);

    @Test
    public void ValidSong() throws Exception {
        Song song = new Song("Title", "Genre", 180, "Artist", 2021);
        assertEquals(song.getArtist(),("Artist"));
    }

    @BeforeEach
    public void setUp() {
        song = new Song();
        transmission = new Transmission();

        transmission.setDatetime(localDateTime);
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
}
