package gr.aueb.radio.services;


import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.domains.Song;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.persistence.SongBroadcastRepository;
import gr.aueb.radio.persistence.SongRepository;
import gr.aueb.radio.representations.SongRepresentation;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SongServiceTest extends IntegrationBase {

    @Inject
    SongRepository songRepository;

    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    SongService songService;

    public SongRepresentation createRepresentation() {
        SongRepresentation songRepresentation = new SongRepresentation();
        songRepresentation.title = "Test Song";
        songRepresentation.artist = "Test Artist";
        songRepresentation.genre = "Test Genre";
        songRepresentation.duration = 5;
        songRepresentation.year = 2021;
        return songRepresentation;
    }

    @Test
    public void testCreateSong() {
        int initNumOfSongs = songRepository.listAll().size();
        SongRepresentation songRepresentation = createRepresentation();
        // Send POST request to create song
        Song createdSong = songService.create(songRepresentation);

        // Verify song was created
        assertNotNull(createdSong.getId());
        assertEquals(songRepresentation.title, createdSong.getTitle());
        assertEquals(songRepresentation.artist, createdSong.getArtist());
        assertEquals(songRepresentation.genre, createdSong.getGenre());
        assertEquals(songRepresentation.duration, createdSong.getDuration());
        assertEquals(songRepresentation.year, createdSong.getYear());
        assertEquals(initNumOfSongs + 1, songRepository.listAll().size());
    }

    @Test
    public void testFindSong() {
        // Create a test song
        int initNumOfSongs = songRepository.listAll().size();
        SongRepresentation songRepresentation = new SongRepresentation();
        songRepresentation.title = "Test Song";
        songRepresentation.artist = "Test Artist";
        songRepresentation.genre = "Test Genre";
        songRepresentation.year = 2022;
        songRepresentation.duration = 18;
        Song createdSong = songService.create(songRepresentation);

        // Send GET request to find song
        SongRepresentation foundSong = songService.findSong(createdSong.getId());

        // Verify song was found
        assertNotNull(foundSong);
        assertEquals(createdSong.getTitle(), foundSong.title);
        assertEquals(createdSong.getArtist(), foundSong.artist);
        assertEquals(createdSong.getGenre(), foundSong.genre);
        assertEquals(createdSong.getDuration(), foundSong.duration);
        assertEquals(createdSong.getYear(), foundSong.year);
        assertThrows(NotFoundException.class, () -> songService.findSong(-1));
    }

    @Test
    public void SongSearchTest(){
        int initNumOfSongs = songRepository.listAll().size();
        SongRepresentation songRepresentation = new SongRepresentation();
        songRepresentation.title = "Test Song";
        songRepresentation.artist = "Test Artist";
        songRepresentation.genre = "Test Genre";
        songRepresentation.year = 2022;
        songRepresentation.duration = 18;
        Song createdSong = songService.create(songRepresentation);
        // should return list all
        List<SongRepresentation> songsFound = songService.search(null, null, null);
        assertEquals(initNumOfSongs + 1, songsFound.size());
        songsFound = songService.search("Test Artist", null, null);
        assertEquals(1, songsFound.size());
        songsFound = songService.search(null, "Test Genre", null);
        assertEquals(1, songsFound.size());
        songsFound = songService.search(null, null, "Test Song");
        assertEquals(1, songsFound.size());
        songsFound = songService.search("Test Artist", "Test Genre", "Test Song");
        assertEquals(1, songsFound.size());

    }
    @Test
    public void testUpdateSong() {
        SongRepresentation songRepresentation = createRepresentation();
        // Try to update immutable song
        Song foundSong = songRepository.listAll().get(0);
        assertThrows(RadioException.class, () -> songService.update(foundSong.getId(), songRepresentation));

        // Try to update song that does not exist
        assertThrows(NotFoundException.class, () -> songService.update(-1, songRepresentation));

        // Create a new song to update
        Song song = songService.create(songRepresentation);

        // Update the song with new values
        SongRepresentation updatedRepresentation = createRepresentation();
        updatedRepresentation.title = "Updated Test Song";
        updatedRepresentation.year = 2022;
        Song updatedSong = songService.update(song.getId(), updatedRepresentation);

        // Verify that the song was updated with the new values
        assertEquals(updatedRepresentation.title, updatedSong.getTitle());
        assertEquals(updatedRepresentation.artist, updatedSong.getArtist());
        assertEquals(updatedRepresentation.genre, updatedSong.getGenre());
        assertEquals(updatedRepresentation.duration, updatedSong.getDuration());
        assertEquals(updatedRepresentation.year, updatedSong.getYear());


    }

    @Test
    public void testDeleteSong() {
        // find an existing song
        Song song = songRepository.listAll().get(0);

        // Get initial number of songs in db
        int originalNumOfSongs = songRepository.listAll().size();
        // Get initial number of songBroadcasts in db
        int originalNumberOfSongBroadcasts = songBroadcastRepository.listAll().size();
        // get number of songbroadcasts for song
        int songNumOfSongBroadcasts = song.getSongBroadcasts().size();

        // try to delete the song
        songService.delete(song.getId());
        assertEquals(originalNumOfSongs - 1, songRepository.listAll().size());
        assertEquals(originalNumberOfSongBroadcasts - songNumOfSongBroadcasts, songRepository.listAll().size());

        // try to delete not found song
        assertThrows(NotFoundException.class, ()->songService.delete(-1));
    }


}
