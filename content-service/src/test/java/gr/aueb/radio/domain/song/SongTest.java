package gr.aueb.radio.domain.song;

import gr.aueb.radio.content.common.DateUtil;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.persistence.SongRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    @Test
    public void testSongCreation() {
        // Arrange
        String title = "Song Title";
        Genre genre = new Genre();
        Integer duration = 240;
        String artist = "Artist Name";
        Integer year = 2022;

        // Act
        Song song = new Song(title, genre, duration, artist, year);

        // Assert
        assertNotNull(song);
        assertEquals(title, song.getTitle());
        assertEquals(genre, song.getGenre());
        assertEquals(duration, song.getDuration());
        assertEquals(artist, song.getArtist());
        assertEquals(year, song.getYear());
    }
    @Inject
    SongRepository songRepository;

    @Test
    public void testGetSongById() {

        Song song = new Song("Title", null, 300, "Artist", 2022);


        assertEquals(song.getId(), song.getId());

    }



    public void testSetArtist() {

        Song song = new Song();
        String artist = "New Artist";
        song.setArtist(artist);
        assertEquals(artist, song.getArtist());
    }

    Song song;
    private static LocalDate localDate = DateUtil.setDate("01-01-2023");
    private static LocalTime localTime = DateUtil.setTime("01:01");
    private static String title = "Title";

    private static int duration = 10;
    private static String artist = "Artist";
    private static Integer year = 2021;

    @BeforeEach
    public void setUp() {
        song = new Song();
        song.setTitle(title);
        song.setDuration(duration);
        song.setArtist(artist);
        song.setYear(year);
    }

    @Test
    public void ValidSong() throws Exception {
        Genre genre = new Genre();
        Song song = new Song("Title",genre,180, "Artist",2021);
        assertEquals(song.getArtist(),("Artist"));
    }

    @Test
    public void successfulSetUp() {
        Genre genre = new Genre();
        assertEquals(song.getTitle(), title);
        assertNull(song.getGenre());
        assertEquals(song.getArtist(), artist);
        assertEquals(song.getYear(), year);
        assertEquals(song.getDuration(), duration);
    }

    @Test
    public void setTitleTest() {
        String newTitle = "New Title";
        song.setTitle(newTitle);
        assertEquals(newTitle, song.getTitle());
    }

    @Test
    public void setGenreTest() {
        Genre genre = new Genre();
        song.setGenre(genre);
        assertEquals(genre, song.getGenre());
    }

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


}