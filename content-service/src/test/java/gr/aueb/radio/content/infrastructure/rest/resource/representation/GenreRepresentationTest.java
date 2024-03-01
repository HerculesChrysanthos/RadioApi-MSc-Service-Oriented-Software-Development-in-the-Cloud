package gr.aueb.radio.content.infrastructure.rest.resource.representation;

import gr.aueb.radio.content.infrastructure.rest.representation.GenreRepresentation;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class GenreRepresentationTest {

    @Test
    public void testSetId() {
        // Create an instance of GenreRepresentation
        GenreRepresentation genreRepresentation = new GenreRepresentation();

        // Set id
        genreRepresentation.setId(1);

        // Check if the id is set correctly
        assertEquals(1, genreRepresentation.id);
    }
}
