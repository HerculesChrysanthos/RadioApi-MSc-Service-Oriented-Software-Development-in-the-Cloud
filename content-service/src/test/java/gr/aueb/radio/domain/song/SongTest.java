package gr.aueb.radio.domain.song;

import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.domain.song.Song;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertNotNull(song);
        Assertions.assertEquals(title, song.getTitle());
        Assertions.assertEquals(genre, song.getGenre());
        Assertions.assertEquals(duration, song.getDuration());
        Assertions.assertEquals(artist, song.getArtist());
        Assertions.assertEquals(year, song.getYear());
    }

    @Test
    public void testSongBroadcasts() {
        // Arrange
        Song song = new Song();
        Integer broadcastId = 1;

        // Act
        song.addSongBroadcast(broadcastId);

        // Assert
        Assertions.assertEquals(1, song.getSongBroadcasts().size());
        Assertions.assertTrue(song.getSongBroadcasts().contains(broadcastId));
    }

    public void testSetArtist() {

        Song song = new Song();
        String artist = "New Artist";
        song.setArtist(artist);
        Assertions.assertEquals(artist, song.getArtist());
    }
}