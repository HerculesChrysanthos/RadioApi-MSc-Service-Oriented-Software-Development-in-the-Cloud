package gr.aueb.radio.content.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GenreRepositoryTest {

    private gr.aueb.radio.content.infrastructure.persistence.GenreRepository genreRepositoryUnderTest;

    @BeforeEach
    @Test
    void setUp() {
        genreRepositoryUnderTest = new gr.aueb.radio.content.infrastructure.persistence.GenreRepository();
    }
}
