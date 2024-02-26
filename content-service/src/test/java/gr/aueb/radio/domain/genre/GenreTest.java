package gr.aueb.radio.domain.genre;

import gr.aueb.radio.content.domain.genre.Genre;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@Transactional
public class GenreTest {

    @Inject
    EntityManager entityManager;

    @Test
    public void testIdAndTitle() {
        // Create a Genre
        Genre genre = new Genre("Rap");
        entityManager.persist(genre);

        // Retrieve the persisted Genre
        Genre persistedGenre = entityManager.find(Genre.class, genre.getId());

        // Test that Genre is not null
        assertNotNull(persistedGenre);

        // Test getId() method
        assertEquals(genre.getId(), persistedGenre.getId());

        // Test getTitle() method
        assertEquals(genre.getTitle(), persistedGenre.getTitle());
    }

    @Test
    public void testSetters() {
        // Create a Genre
        Genre genre = new Genre();

        // Test setId() method
        genre.setId(1);
        assertEquals(1, genre.getId());

        // Test setTitle() method
        genre.setTitle("Rock");
        assertEquals("Rock", genre.getTitle());
    }
}
