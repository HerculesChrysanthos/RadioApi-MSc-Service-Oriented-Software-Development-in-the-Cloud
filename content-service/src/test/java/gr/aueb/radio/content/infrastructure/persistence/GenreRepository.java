package gr.aueb.radio.content.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;

public class GenreRepository {

    private gr.aueb.radio.content.infrastructure.persistence.GenreRepository genreRepositoryUnderTest;

    @BeforeEach
    void setUp() {
        genreRepositoryUnderTest = new gr.aueb.radio.content.infrastructure.persistence.GenreRepository();
    }
}
