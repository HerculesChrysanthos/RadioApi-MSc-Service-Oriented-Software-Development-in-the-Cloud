package gr.aueb.radio.content.infrastructure.rest.representation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenreRepresentationTest {

    private GenreRepresentation genreRepresentationUnderTest;

    @BeforeEach
    void setUp() {
        genreRepresentationUnderTest = new GenreRepresentation();
    }

    @Test
    void testGetId() {
        assertEquals(0, genreRepresentationUnderTest.getId());
    }
}
