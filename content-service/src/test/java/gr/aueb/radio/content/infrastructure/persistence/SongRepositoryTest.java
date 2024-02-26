package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.song.Song;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class SongRepositoryTest {

    @Inject
    SongRepository songRepository;
//    @Test
//    @Transactional
//    public void testFindSongsByGenre() {
//
//        Genre hipHopGenre = new Genre("Hip Hop");
//        List<Song> genreHiphop = songRepository.findSongsByGenre(hipHopGenre);
//
//        for (Song song : genreHiphop) {
//            assertEquals("Hip Hop", song.getGenre());
//        }
//
//    }
//    @Test
//    @Transactional
//    public void testFindAllSongsByGenre() {
//
//
//        List<String> songsbygenre  = songRepository.getAllGenres();
//
//        assertEquals("Hip hop", songsbygenre.get(0));
//
//    }
    private SongRepository songRepositoryUnderTest;

    @BeforeEach
    void setUp() {
        songRepositoryUnderTest = new SongRepository();
    }

    @Test
    void testFindSongsByGenre() {
        // Setup
        // Run the test
        //final List<Song> result = songRepositoryUnderTest.findSongsByGenre("genre");
        assertThrows(java.lang.IllegalArgumentException.class, () -> {
            songRepositoryUnderTest.findSongsByGenre("genre");
        });

        // Verify the results
    }

    @Test
    void testGetAllGenres() {
        assertThrows(java.lang.IllegalStateException.class, () -> {
            songRepositoryUnderTest.getAllGenres();
        });
    }

    @Test
    void testFindSongsByIds() {
        // Setup
        // Run the test
        final List<Song> result = songRepositoryUnderTest.findSongsByIds(List.of(0));

        // Verify the results
    }

    @Test
    void testFindByFilters() {
        // Setup
        // Run the test
        final List<Song> result = songRepositoryUnderTest.findByFilters("artist", 0, "genreTitle", "title", List.of(0));

        // Verify the results
    }

}