package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.application.SongService;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.rest.representation.SongInputDTO;
import gr.aueb.radio.content.infrastructure.rest.representation.SongMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.SongRepresentation;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class SongResourceTest {

    @Mock
    private UriInfo mockUriInfo;
    @Mock
    private SongMapper mockSongMapper;
    @Mock
    private SongService mockSongService;

    private SongResource songResourceUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        songResourceUnderTest = new SongResource();
        songResourceUnderTest.uriInfo = mockUriInfo;
        songResourceUnderTest.songMapper = mockSongMapper;
        songResourceUnderTest.songService = mockSongService;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testGetSong() {
        // Setup
        when(mockSongService.findSong(0, "auth")).thenReturn(new SongRepresentation());

        // Run the test
        final Response result = songResourceUnderTest.getSong(0, "auth");

        // Verify the results
    }

    @Test
    void testSearch() {
        // Setup
        when(mockSongService.search("artist", "genre", "title", List.of(0), "auth"))
                .thenReturn(List.of(new SongRepresentation()));

        // Run the test
        final Response result = songResourceUnderTest.search("artist", "genre", "title", "songsIds", "auth");

        // Verify the results
    }

    @Test
    void testSearch_SongServiceReturnsNoItems() {
        // Setup
        when(mockSongService.search("artist", "genre", "title", List.of(0), "auth"))
                .thenReturn(Collections.emptyList());

        // Run the test
        final Response result = songResourceUnderTest.search("artist", "genre", "title", "songsIds", "auth");

        // Verify the results
    }

    @Test
    void testCreate() {
        // Setup
        final SongInputDTO songRepresentation = new SongInputDTO();

        // Configure SongService.create(...).
        final Genre genre = new Genre();
        final Song song = new Song("title", genre, 0, "artist", 2020);
        when(mockSongService.create(any(SongRepresentation.class), eq("auth"))).thenReturn(song);

        when(mockSongMapper.toRepresentation(any(Song.class))).thenReturn(new SongRepresentation());

        // Run the test
        final Response result = songResourceUnderTest.create(songRepresentation, "auth");

        // Verify the results
    }

    @Test
    void testDelete() {
        // Setup
        // Run the test
        final Response result = songResourceUnderTest.delete(0, "auth");

        // Verify the results
        verify(mockSongService).delete(0, "auth");
    }

    @Test
    void testUpdate() {
        // Setup
        final SongInputDTO songRepresentation = new SongInputDTO();

        // Run the test
        final Response result = songResourceUnderTest.update(0, songRepresentation, "auth");

        // Verify the results
        verify(mockSongService).update(eq(0), any(SongRepresentation.class), eq("auth"));
    }
}
