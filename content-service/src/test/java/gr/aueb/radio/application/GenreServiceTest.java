package gr.aueb.radio.application;

import gr.aueb.radio.content.application.GenreService;
import gr.aueb.radio.content.common.NotFoundException;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.infrastructure.persistence.GenreRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GenreServiceTest {

    @Mock
    private GenreRepository mockGenreRepository;
    @Mock
    private GenreMapper mockGenreMapper;

    private GenreService genreServiceUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        genreServiceUnderTest = new GenreService();
        genreServiceUnderTest.genreRepository = mockGenreRepository;
        genreServiceUnderTest.genreMapper = mockGenreMapper;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testGetGenreById() {
        // Setup
        // Configure GenreRepository.findById(...).
        final Genre genre = new Genre();
        when(mockGenreRepository.findById(0)).thenReturn(genre);

        when(mockGenreMapper.toRepresentation(any(Genre.class))).thenReturn(new GenreRepresentation());

        // Run the test
        final GenreRepresentation result = genreServiceUnderTest.getGenreById(0);

        // Verify the results
    }

    @Test
    void testGetGenreById_GenreRepositoryReturnsNull() {
        // Setup
        when(mockGenreRepository.findById(0)).thenReturn(null);

        // Run the test
        assertThrows(NotFoundException.class, () -> genreServiceUnderTest.getGenreById(0));
    }
}
