package gr.aueb.radio.content.application;

import gr.aueb.radio.content.common.IntegrationBase;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.persistence.SongRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.*;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@QuarkusTest
class SongServiceTest extends IntegrationBase {

    @Inject
    SongService songService;

    @InjectMock
    UserService userService;

    @BeforeEach
    public void setup(){
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "USER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);
    }

    @Test
    public void testCreateSong() {
        // Create a test song
        SongInputDTO songInputDTO = new SongInputDTO();
        GenreRepresentation genreRepresentation = new GenreRepresentation();
        genreRepresentation.id = 1;
        genreRepresentation.title = "genre-title";

        songInputDTO.title = "Test Song";
        songInputDTO.artist = "Test Artist";
        songInputDTO.genreId = 1;
        songInputDTO.year = 2022;
        songInputDTO.duration = 18;

        Song createdSong = songService.create(songInputDTO, "test");

        SongRepresentation foundSong = songService.findSong(createdSong.getId(), "auth");

        assertNotNull(foundSong);
        assertEquals(createdSong.getTitle(), foundSong.title);
        assertEquals(createdSong.getArtist(), foundSong.artist);
        assertEquals(createdSong.getGenre().getId(), foundSong.genre.id);
        assertEquals(createdSong.getDuration(), foundSong.duration);
        assertEquals(createdSong.getYear(), foundSong.year);
        assertThrows(NotFoundException.class, () -> songService.findSong(-1, "auth"));
    }

    @Test
    public void testSearch() {
        List<SongRepresentation> found = songService.search("Twenty One Pilots", 1, "Hip hop", "Ride", null, "auth");

        assertNotNull(found);
        assertEquals("Ride", found.get(0).title);
    }

    @Test
    public void testFindSong() {
        SongRepresentation foundSong = songService.findSong(7001, "auth");

        // Verify song was found
        assertNotNull(foundSong);
        assertEquals("Ride", foundSong.title);
        assertEquals("Twenty One Pilots", foundSong.artist);
        assertEquals(1, foundSong.genre.id);
        assertEquals(25, foundSong.duration);
        assertEquals(2015, foundSong.year);
        assertThrows(NotFoundException.class, () -> songService.findSong(-1, "auth"));
    }



//    @Mock
//    private SongRepository mockSongRepository;
//    @Mock
//    private GenreService mockGenreService;
//    @Mock
//    private SongMapper mockSongMapper;
//    @Mock
//    private GenreMapper mockGenreMapper;
//    @Mock
//    private UserService mockUserService;
//
//    private SongService songServiceUnderTest;
//
//    private AutoCloseable mockitoCloseable;
//
//    @BeforeEach
//    void setUp() {
//        mockitoCloseable = openMocks(this);
//        songServiceUnderTest = new SongService();
//        songServiceUnderTest.songRepository = mockSongRepository;
//        songServiceUnderTest.genreService = mockGenreService;
//        songServiceUnderTest.songMapper = mockSongMapper;
//        songServiceUnderTest.genreMapper = mockGenreMapper;
//        songServiceUnderTest.userService = mockUserService;
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        mockitoCloseable.close();
//    }
//
//    @Test
//    void testSearch() {
//        // Setup
//        // Configure SongRepository.findByFilters(...).
//        final Song song = new Song();
//        song.setTitle("title");
//        song.setArtist("artist");
//        song.setDuration(0);
//        final Genre genre = new Genre();
//        song.setGenre(genre);
//        song.setYear(2020);
//        final List<Song> songs = List.of(song);
//        when(mockSongRepository.findByFilters("artist", 0, "genreTitle", "title", List.of(0))).thenReturn(songs);
//
//        // Configure SongMapper.toRepresentationList(...).
//        final Song song1 = new Song();
//        song1.setTitle("title");
//        song1.setArtist("artist");
//        song1.setDuration(0);
//        final Genre genre1 = new Genre();
//        song1.setGenre(genre1);
//        song1.setYear(2020);
//        final List<Song> songs1 = List.of(song1);
//        when(mockSongMapper.toRepresentationList(songs1)).thenReturn(List.of(new SongRepresentation()));
//
//        // Run the test
//        final List<SongRepresentation> result = songServiceUnderTest.search("artist", 0, "genreTitle", "title",
//                List.of(0), "auth");
//
//        // Verify the results
//        verify(mockUserService).verifyAuth("auth");
//    }
//
//    @Test
//    void testSearch_SongRepositoryReturnsNoItems() {
//        // Setup
//        when(mockSongRepository.findByFilters("artist", 0, "genreTitle", "title", List.of(0)))
//                .thenReturn(Collections.emptyList());
//
//        // Configure SongMapper.toRepresentationList(...).
//        final Song song = new Song();
//        song.setTitle("title");
//        song.setArtist("artist");
//        song.setDuration(0);
//        final Genre genre = new Genre();
//        song.setGenre(genre);
//        song.setYear(2020);
//        final List<Song> songs = List.of(song);
//        when(mockSongMapper.toRepresentationList(songs)).thenReturn(List.of(new SongRepresentation()));
//
//        // Run the test
//        final List<SongRepresentation> result = songServiceUnderTest.search("artist", 0, "genreTitle", "title",
//                List.of(0), "auth");
//
//        // Verify the results
//        verify(mockUserService).verifyAuth("auth");
//    }
//
//    @Test
//    void testSearch_SongMapperReturnsNoItems() {
//        // Setup
//        // Configure SongRepository.findByFilters(...).
//        final Song song = new Song();
//        song.setTitle("title");
//        song.setArtist("artist");
//        song.setDuration(0);
//        final Genre genre = new Genre();
//        song.setGenre(genre);
//        song.setYear(2020);
//        final List<Song> songs = List.of(song);
//        when(mockSongRepository.findByFilters("artist", 0, "genreTitle", "title", List.of(0))).thenReturn(songs);
//
//        // Configure SongMapper.toRepresentationList(...).
//        final Song song1 = new Song();
//        song1.setTitle("title");
//        song1.setArtist("artist");
//        song1.setDuration(0);
//        final Genre genre1 = new Genre();
//        song1.setGenre(genre1);
//        song1.setYear(2020);
//        final List<Song> songs1 = List.of(song1);
//        when(mockSongMapper.toRepresentationList(songs1)).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final List<SongRepresentation> result = songServiceUnderTest.search("artist", 0, "genreTitle", "title",
//                List.of(0), "auth");
//
//        // Verify the results
//        assertEquals(Collections.emptyList(), result);
//        verify(mockUserService).verifyAuth("auth");
//    }
//
//    @Test
//    void testUpdate() {
//        // Setup
//        final SongRepresentation songRepresentation = new SongRepresentation();
//
//        // Configure SongRepository.findById(...).
//        final Song song = new Song();
//        song.setTitle("title");
//        song.setArtist("artist");
//        song.setDuration(0);
//        final Genre genre = new Genre();
//        song.setGenre(genre);
//        song.setYear(2020);
//        when(mockSongRepository.findById(0)).thenReturn(song);
//
//        when(mockGenreService.getGenreById(0)).thenReturn(new GenreRepresentation());
//        when(mockGenreMapper.toModel(any(GenreRepresentation.class))).thenReturn(new Genre("title"));
//
//        // Configure SongRepository.getEntityManager(...).
//        final EntityManager mockEntityManager = mock(EntityManager.class);
//        when(mockSongRepository.getEntityManager()).thenReturn(mockEntityManager);
//
//        // Run the test
//        //final Song result = songServiceUnderTest.update(0, songRepresentation, "auth");
//
//        assertThrows(java.lang.NullPointerException.class, () -> songServiceUnderTest.update(0, songRepresentation, "auth"));
//
//        // Verify the results
//        verify(mockUserService).verifyAuth("auth");
//        //verify(mockEntityManager).close();
//    }
//
//    @Test
//    void testUpdate_SongRepositoryFindByIdReturnsNull() {
//        // Setup
//        final SongRepresentation songRepresentation = new SongRepresentation();
//        when(mockSongRepository.findById(0)).thenReturn(null);
//
//        // Run the test
//        assertThrows(NotFoundException.class, () -> songServiceUnderTest.update(0, songRepresentation, "auth"));
//        verify(mockUserService).verifyAuth("auth");
//    }
//
//    @Test
//    void testDelete() {
//        // Setup
//        // Configure SongRepository.findById(...).
//        final Song song = new Song();
//        song.setTitle("title");
//        song.setArtist("artist");
//        song.setDuration(0);
//        final Genre genre = new Genre();
//        song.setGenre(genre);
//        song.setYear(2020);
//        when(mockSongRepository.findById(0)).thenReturn(song);
//
//        // Run the test
//        songServiceUnderTest.delete(0, "auth");
//
//        // Verify the results
//        verify(mockUserService).verifyAuth("auth");
//        verify(mockSongRepository).deleteById(0);
//    }
//
//    @Test
//    void testDelete_SongRepositoryFindByIdReturnsNull() {
//        // Setup
//        when(mockSongRepository.findById(0)).thenReturn(null);
//
//        // Run the test
//        assertThrows(NotFoundException.class, () -> songServiceUnderTest.delete(0, "auth"));
//        verify(mockUserService).verifyAuth("auth");
//    }
//
//    @Test
//    void testFindSong() {
//        // Setup
//        // Configure SongRepository.findById(...).
//        final Song song = new Song();
//        song.setTitle("title");
//        song.setArtist("artist");
//        song.setDuration(0);
//        final Genre genre = new Genre();
//        song.setGenre(genre);
//        song.setYear(2020);
//        when(mockSongRepository.findById(0)).thenReturn(song);
//
//        when(mockSongMapper.toRepresentation(any(Song.class))).thenReturn(new SongRepresentation());
//
//        // Run the test
//        final SongRepresentation result = songServiceUnderTest.findSong(0, "auth");
//
//        // Verify the results
//        verify(mockUserService).verifyAuth("auth");
//    }
//
//    @Test
//    void testFindSong_SongRepositoryReturnsNull() {
//        // Setup
//        when(mockSongRepository.findById(0)).thenReturn(null);
//
//        // Run the test
//        assertThrows(NotFoundException.class, () -> songServiceUnderTest.findSong(0, "auth"));
//        verify(mockUserService).verifyAuth("auth");
//    }
//
//    @Test
//    void testCreate() {
//        // Setup
//        final SongInputDTO songInputDTO = new SongInputDTO();
//
//        when(mockGenreService.getGenreById(0)).thenReturn(new GenreRepresentation());
//
//        // Configure SongMapper.toModel(...).
//        final Song song = new Song();
//        song.setTitle("title");
//        song.setArtist("artist");
//        song.setDuration(0);
//        final Genre genre = new Genre();
//        song.setGenre(genre);
//        song.setYear(2020);
//        when(mockSongMapper.toModel(any(SongRepresentation.class))).thenReturn(song);
//
//        // Run the test
//        final Song result = songServiceUnderTest.create(songInputDTO, "auth");
//
//        // Verify the results
//        verify(mockUserService).verifyAuth("auth");
//        verify(mockSongRepository).persist(any(Song.class));
//    }
}
