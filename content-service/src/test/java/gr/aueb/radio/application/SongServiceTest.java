package gr.aueb.radio.application;

import gr.aueb.radio.content.application.GenreService;
import gr.aueb.radio.content.application.SongService;
import gr.aueb.radio.content.application.UserService;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.persistence.SongRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreRepresentation;
import gr.aueb.radio.content.infrastructure.rest.representation.SongMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.SongRepresentation;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class SongServiceTest {
    @Mock
    SongRepository songRepository;
    @Mock
    GenreService genreService;
    @Mock
    SongMapper songMapper;
    @Mock
    GenreMapper genreMapper;
    @Mock
    UserService userService;
    @InjectMocks
    SongService songService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearch() {
        when(songRepository.findSongsByIds(any())).thenReturn(List.of(new Song("title", null, Integer.valueOf(0), "artist", Integer.valueOf(0))));
        when(songMapper.toRepresentationList(any())).thenReturn(List.of(new SongRepresentation()));
        when(userService.verifyAuth(anyString())).thenReturn(new UserVerifiedRepresentation());

        List<SongRepresentation> result = songService.search("artist", "genre", "title", List.of(Integer.valueOf(0)), "auth");
        Assertions.assertEquals(List.of(new SongRepresentation()), result);
    }

    @Test
    void testUpdate() {
        when(genreService.getGenreById(anyInt())).thenReturn(new GenreRepresentation());
        when(genreMapper.toModel(any())).thenReturn(new Genre());
        when(userService.verifyAuth(anyString())).thenReturn(new UserVerifiedRepresentation());

        Song result = songService.update(Integer.valueOf(0), new SongRepresentation(), "auth");
        Assertions.assertEquals(new Song("title", null, Integer.valueOf(0), "artist", Integer.valueOf(0)), result);
    }

    @Test
    void testDelete() {
        when(userService.verifyAuth(anyString())).thenReturn(new UserVerifiedRepresentation());

        songService.delete(Integer.valueOf(0), "auth");
    }

    @Test
    void testFindSong() {
        when(songMapper.toRepresentation(any())).thenReturn(new SongRepresentation());
        when(userService.verifyAuth(anyString())).thenReturn(new UserVerifiedRepresentation());

        SongRepresentation result = songService.findSong(Integer.valueOf(0), "auth");
        Assertions.assertEquals(new SongRepresentation(), result);
    }

    @Test
    void testCreate() {
        when(songMapper.toModel(any())).thenReturn(new Song("title", null, Integer.valueOf(0), "artist", Integer.valueOf(0)));
        when(userService.verifyAuth(anyString())).thenReturn(new UserVerifiedRepresentation());

        Song result = songService.create(new SongRepresentation(), "auth");
        Assertions.assertEquals(new Song("title", null, Integer.valueOf(0), "artist", Integer.valueOf(0)), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme