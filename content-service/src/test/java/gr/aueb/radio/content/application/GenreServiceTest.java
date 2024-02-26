package gr.aueb.radio.content.application;

import gr.aueb.radio.content.infrastructure.persistence.GenreRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

    @InjectMocks
    GenreService genreService; // The service class that contains getGenreById method

    @Mock
    GenreRepository genreRepository; // Mocking the repository dependency

    @Mock
    GenreMapper genreMapper;

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

//    @Test
//    void testGetGenreById() {
//        // Setup
//        // Configure GenreRepository.findById(...).
//        final Genre genre = new Genre();
//        when(mockGenreRepository.findById(0)).thenReturn(genre);
//
//        when(mockGenreMapper.toRepresentation(any(Genre.class))).thenReturn(new GenreRepresentation());
//
//        // Run the test
//        final GenreRepresentation result = genreServiceUnderTest.getGenreById(0);
//
//        // Verify the results
//    }

//    @Test
//    public void testGetGenreById() {
//        // Create a sample genre and representation
//        Genre genre = new Genre();
//        genre.setId(2);
//        genre.setTitle("Rock");
//
//        GenreRepresentation expectedRepresentation = new GenreRepresentation();
//        expectedRepresentation.setId(1);
//
//        // Mocking behavior of repository and mapper
//        Mockito.when(genreRepository.findById(1)).thenReturn(genre);
//        Mockito.when(genreMapper.toRepresentation(genre)).thenReturn(expectedRepresentation);
//
//        // Call the service method
//        GenreRepresentation actualRepresentation = genreService.getGenreById(1);
//
//        // Verify that the service method returns the expected representation
//        //assertEquals(expectedRepresentation.getId(), actualRepresentation.getId());
//    }

    @Test
    void testGetGenreById() {
        when(genreMapper.toRepresentation(any())).thenReturn(new GenreRepresentation());

        GenreRepresentation result = genreService.getGenreById(Integer.valueOf(1));
        Assertions.assertEquals(new GenreRepresentation(), result);
    }

//    @Test
//    public void testGetGenreByIdNotFound() {
//        // Mocking behavior of repository to return null
//        Mockito.when(genreRepository.findById(1)).thenReturn(null);
//
//        // Verify that NotFoundException is thrown when genre is not found
//        assertThrows(NotFoundException.class, () -> {
//            genreService.getGenreById(1);
//        });
//    }
//
//    @Test
//    void testGetGenreById_GenreRepositoryReturnsNull() {
//        // Setup
//        Mockito.when(genreRepository.findById(1)).thenReturn(null);
//
//        // Run the test
//        assertThrows(NotFoundException.class, () -> genreServiceUnderTest.getGenreById(0));
//    }
}
