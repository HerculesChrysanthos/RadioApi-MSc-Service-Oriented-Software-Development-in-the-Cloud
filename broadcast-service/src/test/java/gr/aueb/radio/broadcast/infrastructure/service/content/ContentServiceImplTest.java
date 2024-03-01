package gr.aueb.radio.broadcast.infrastructure.service.content;

import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.*;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContentServiceImplTest {

    @Mock
    private ContentApi mockContentApi;

    private ContentServiceImpl contentServiceImpl;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        contentServiceImpl = new ContentServiceImpl();
        contentServiceImpl.contentApi = mockContentApi;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    public void testGetAd() {
        when(mockContentApi.getAd("auth", 1)).thenReturn(new AdBasicRepresentation());

        AdBasicRepresentation result = contentServiceImpl.getAd("auth", 1);
        assertNotNull(result);
    }

    @Test
    public void testGetAdProcessingException() {
        when(mockContentApi.getAd("auth", 1)).thenThrow(new ProcessingException(""));
        assertThrows(ExternalServiceException.class, () -> contentServiceImpl.getAd("auth", 1));
    }

    @Test
    public void testGetAdWebApplicationException() {
        when(mockContentApi.getAd("auth", 1)).thenThrow(new WebApplicationException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getAd("auth", 1));
    }

    @Test
    public void testGetSong() {
        when(mockContentApi.getSongId("auth", 1)).thenReturn(new SongBasicRepresentation());

        SongBasicRepresentation result = contentServiceImpl.getSong("auth", 1);
        assertNotNull(result);
    }

    @Test
    public void testGetSongProcessingException() {
        when(mockContentApi.getSongId("auth", 1)).thenThrow(new ProcessingException(""));
        assertThrows(ExternalServiceException.class, () -> contentServiceImpl.getSong("auth", 1));
    }

    @Test
    public void testGetSongNotFoundException() {
        when(mockContentApi.getSongId("auth", 1)).thenThrow(new NotFoundException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getSong("auth", 1));
    }

    @Test
    public void testGetSongsByFilters() {

        List<SongBasicRepresentation> s = new ArrayList<>();
        when(mockContentApi.getSongsByFilters("auth", "",1, "", "","")).thenReturn(s);

        List<SongBasicRepresentation> result = contentServiceImpl.getSongsByFilters("auth", "",1, "", "","");
        assertNotNull(result);
    }

    @Test
    public void testGetSongsByFiltersProcessingException() {
        when(mockContentApi.getSongsByFilters("auth", "",1, "", "","")).thenThrow(new ProcessingException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getSongsByFilters("auth", "",1, "", "",""));
    }

    @Test
    public void testGetSongsByFiltersNotFoundException() {
        when(mockContentApi.getSongsByFilters("auth", "",1, "", "","")).thenThrow(new NotFoundException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getSongsByFilters("auth", "",1, "", "",""));
    }

    @Test
    public void testGetAdsByFilters() {

        List<AdBasicRepresentation> s = new ArrayList<>();
        when(mockContentApi.getAdsByFilters("auth", "","")).thenReturn(s);

        List<AdBasicRepresentation> result = contentServiceImpl.getAdsByFilters("auth", "","");
        assertNotNull(result);
    }

    @Test
    public void testGetAdsByFiltersProcessingException() {
        when(mockContentApi.getAdsByFilters("auth", "","")).thenThrow(new ProcessingException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getAdsByFilters("auth", "",""));
    }

    @Test
    public void testGetAdsByFiltersNotFoundException() {
        when(mockContentApi.getAdsByFilters("auth", "","")).thenThrow(new NotFoundException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getAdsByFilters("auth", "",""));
    }


    @Test
    public void testGetGenreById() {
        when(mockContentApi.getGenreById(1,"auth")).thenReturn(new GenreBasicRepresentation());

        GenreBasicRepresentation result = contentServiceImpl.getGenreById(1,"auth");
        assertNotNull(result);
    }

    @Test
    public void testGetGenreByIdProcessingException() {
        when(mockContentApi.getGenreById(1,"auth")).thenThrow(new ProcessingException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getGenreById(1,"auth"));
    }

    @Test
    public void testGetGenreByIdNotFoundException() {
        when(mockContentApi.getGenreById(1,"auth")).thenThrow(new NotFoundException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getGenreById(1,"auth"));
    }

    @Test
    public void testGetAllGenres() {
        List<GenreBasicRepresentation> g = new ArrayList<>();
        when(mockContentApi.getAllGenres("auth")).thenReturn(g);

        List<GenreBasicRepresentation>  result = contentServiceImpl.getAllGenres("auth");
        assertNotNull(result);
    }

    @Test
    public void testGetAllGenresProcessingException() {
        when(mockContentApi.getAllGenres("auth")).thenThrow(new ProcessingException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getAllGenres("auth"));
    }

    @Test
    public void testGetAllGenresNotFoundException() {
        when(mockContentApi.getAllGenres("auth")).thenThrow(new NotFoundException(""));
        assertThrows(RadioException.class, () -> contentServiceImpl.getAllGenres("auth"));
    }

}


