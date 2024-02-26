package gr.aueb.radio.content.application;

import gr.aueb.radio.content.common.NotFoundException;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.persistence.SongRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SongServiceTest {

    @Mock
    private SongRepository mockSongRepository;
    @Mock
    private GenreService mockGenreService;
    @Mock
    private SongMapper mockSongMapper;
    @Mock
    private GenreMapper mockGenreMapper;
    @Mock
    private UserService mockUserService;

    private SongService songServiceUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        songServiceUnderTest = new SongService();
        songServiceUnderTest.songRepository = mockSongRepository;
        songServiceUnderTest.genreService = mockGenreService;
        songServiceUnderTest.songMapper = mockSongMapper;
        songServiceUnderTest.genreMapper = mockGenreMapper;
        songServiceUnderTest.userService = mockUserService;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testSearch() {
        // Setup
        // Configure SongRepository.findByFilters(...).
        final Song song = new Song();
        song.setTitle("title");
        song.setArtist("artist");
        song.setDuration(0);
        final Genre genre = new Genre();
        song.setGenre(genre);
        song.setYear(2020);
        final List<Song> songs = List.of(song);
        when(mockSongRepository.findByFilters("artist", 0, "genreTitle", "title", List.of(0))).thenReturn(songs);

        // Configure SongMapper.toRepresentationList(...).
        final Song song1 = new Song();
        song1.setTitle("title");
        song1.setArtist("artist");
        song1.setDuration(0);
        final Genre genre1 = new Genre();
        song1.setGenre(genre1);
        song1.setYear(2020);
        final List<Song> songs1 = List.of(song1);
        when(mockSongMapper.toRepresentationList(songs1)).thenReturn(List.of(new SongRepresentation()));

        // Run the test
        final List<SongRepresentation> result = songServiceUnderTest.search("artist", 0, "genreTitle", "title",
                List.of(0), "auth");

        // Verify the results
        verify(mockUserService).verifyAuth("auth");
    }

    @Test
    void testSearch_SongRepositoryReturnsNoItems() {
        // Setup
        when(mockSongRepository.findByFilters("artist", 0, "genreTitle", "title", List.of(0)))
                .thenReturn(Collections.emptyList());

        // Configure SongMapper.toRepresentationList(...).
        final Song song = new Song();
        song.setTitle("title");
        song.setArtist("artist");
        song.setDuration(0);
        final Genre genre = new Genre();
        song.setGenre(genre);
        song.setYear(2020);
        final List<Song> songs = List.of(song);
        when(mockSongMapper.toRepresentationList(songs)).thenReturn(List.of(new SongRepresentation()));

        // Run the test
        final List<SongRepresentation> result = songServiceUnderTest.search("artist", 0, "genreTitle", "title",
                List.of(0), "auth");

        // Verify the results
        verify(mockUserService).verifyAuth("auth");
    }

    @Test
    void testSearch_SongMapperReturnsNoItems() {
        // Setup
        // Configure SongRepository.findByFilters(...).
        final Song song = new Song();
        song.setTitle("title");
        song.setArtist("artist");
        song.setDuration(0);
        final Genre genre = new Genre();
        song.setGenre(genre);
        song.setYear(2020);
        final List<Song> songs = List.of(song);
        when(mockSongRepository.findByFilters("artist", 0, "genreTitle", "title", List.of(0))).thenReturn(songs);

        // Configure SongMapper.toRepresentationList(...).
        final Song song1 = new Song();
        song1.setTitle("title");
        song1.setArtist("artist");
        song1.setDuration(0);
        final Genre genre1 = new Genre();
        song1.setGenre(genre1);
        song1.setYear(2020);
        final List<Song> songs1 = List.of(song1);
        when(mockSongMapper.toRepresentationList(songs1)).thenReturn(Collections.emptyList());

        // Run the test
        final List<SongRepresentation> result = songServiceUnderTest.search("artist", 0, "genreTitle", "title",
                List.of(0), "auth");

        // Verify the results
        assertEquals(Collections.emptyList(), result);
        verify(mockUserService).verifyAuth("auth");
    }

    @Test
    void testUpdate() {
        // Setup
        final SongRepresentation songRepresentation = new SongRepresentation();

        // Configure SongRepository.findById(...).
        final Song song = new Song();
        song.setTitle("title");
        song.setArtist("artist");
        song.setDuration(0);
        final Genre genre = new Genre();
        song.setGenre(genre);
        song.setYear(2020);
        when(mockSongRepository.findById(0)).thenReturn(song);

        when(mockGenreService.getGenreById(0)).thenReturn(new GenreRepresentation());
        when(mockGenreMapper.toModel(any(GenreRepresentation.class))).thenReturn(new Genre("title"));

        // Configure SongRepository.getEntityManager(...).
        final EntityManager mockEntityManager = mock(EntityManager.class);
        when(mockSongRepository.getEntityManager()).thenReturn(mockEntityManager);

        // Run the test
        final Song result = songServiceUnderTest.update(0, songRepresentation, "auth");

        // Verify the results
        verify(mockUserService).verifyAuth("auth");
        verify(mockEntityManager).close();
    }

    @Test
    void testUpdate_SongRepositoryFindByIdReturnsNull() {
        // Setup
        final SongRepresentation songRepresentation = new SongRepresentation();
        when(mockSongRepository.findById(0)).thenReturn(null);

        // Run the test
        assertThrows(NotFoundException.class, () -> songServiceUnderTest.update(0, songRepresentation, "auth"));
        verify(mockUserService).verifyAuth("auth");
    }

    @Test
    void testDelete() {
        // Setup
        // Configure SongRepository.findById(...).
        final Song song = new Song();
        song.setTitle("title");
        song.setArtist("artist");
        song.setDuration(0);
        final Genre genre = new Genre();
        song.setGenre(genre);
        song.setYear(2020);
        when(mockSongRepository.findById(0)).thenReturn(song);

        // Run the test
        songServiceUnderTest.delete(0, "auth");

        // Verify the results
        verify(mockUserService).verifyAuth("auth");
        verify(mockSongRepository).deleteById(0);
    }

    @Test
    void testDelete_SongRepositoryFindByIdReturnsNull() {
        // Setup
        when(mockSongRepository.findById(0)).thenReturn(null);

        // Run the test
        assertThrows(NotFoundException.class, () -> songServiceUnderTest.delete(0, "auth"));
        verify(mockUserService).verifyAuth("auth");
    }

    @Test
    void testFindSong() {
        // Setup
        // Configure SongRepository.findById(...).
        final Song song = new Song();
        song.setTitle("title");
        song.setArtist("artist");
        song.setDuration(0);
        final Genre genre = new Genre();
        song.setGenre(genre);
        song.setYear(2020);
        when(mockSongRepository.findById(0)).thenReturn(song);

        when(mockSongMapper.toRepresentation(any(Song.class))).thenReturn(new SongRepresentation());

        // Run the test
        final SongRepresentation result = songServiceUnderTest.findSong(0, "auth");

        // Verify the results
        verify(mockUserService).verifyAuth("auth");
    }

    @Test
    void testFindSong_SongRepositoryReturnsNull() {
        // Setup
        when(mockSongRepository.findById(0)).thenReturn(null);

        // Run the test
        assertThrows(NotFoundException.class, () -> songServiceUnderTest.findSong(0, "auth"));
        verify(mockUserService).verifyAuth("auth");
    }

    @Test
    void testCreate() {
        // Setup
        final SongInputDTO songInputDTO = new SongInputDTO();

        when(mockGenreService.getGenreById(0)).thenReturn(new GenreRepresentation());

        // Configure SongMapper.toModel(...).
        final Song song = new Song();
        song.setTitle("title");
        song.setArtist("artist");
        song.setDuration(0);
        final Genre genre = new Genre();
        song.setGenre(genre);
        song.setYear(2020);
        when(mockSongMapper.toModel(any(SongRepresentation.class))).thenReturn(song);

        // Run the test
        final Song result = songServiceUnderTest.create(songInputDTO, "auth");

        // Verify the results
        verify(mockUserService).verifyAuth("auth");
        verify(mockSongRepository).persist(any(Song.class));
    }
}
