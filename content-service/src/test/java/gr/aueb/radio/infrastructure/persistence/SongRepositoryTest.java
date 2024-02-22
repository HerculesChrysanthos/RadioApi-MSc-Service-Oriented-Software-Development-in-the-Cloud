package gr.aueb.radio.infrastructure.persistence;

import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.persistence.SongRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class SongRepositoryTest {

    @Inject
    SongRepository songRepository;
    @Test
    @Transactional
    public void testFindSongsByGenre() {

        List<Song> genreHiphop = songRepository.findSongsByGenre("Hip Hop");

        for (Song song : genreHiphop) {
            assertEquals("Hip Hop", song.getGenre());
        }

    }
    @Test
    @Transactional
    public void testFindAllSongsByGenre() {


        List<String> songsbygenre  = songRepository.getAllGenres();

        assertEquals("Hip hop", songsbygenre.get(0));

    }

}