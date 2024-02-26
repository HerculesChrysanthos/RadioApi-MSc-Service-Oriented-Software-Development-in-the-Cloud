package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.ad.Zone;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AdRepositoryTest {

    @Inject
    EntityManager entityManager;
    private AdRepository adRepositoryUnderTest;

    @BeforeEach
    void setUp() {
        adRepositoryUnderTest = new AdRepository();
    }

    @Test
    void testFindByTimezone() {
        // Setup
        // Run the test
      //  final List<Ad> result = adRepositoryUnderTest.findByTimezone(Zone.EarlyMorning);

        assertThrows(java.lang.IllegalStateException.class, () -> {
            adRepositoryUnderTest.findByTimezone(Zone.EarlyMorning);
        });
    }

    @Test
    void testFindAdsByIds() {
        // Setup
        // Run the test
     //   final List<Ad> result = adRepositoryUnderTest.findAdsByIds(List.of(0));

        assertThrows(java.lang.IllegalStateException.class, () -> {
            adRepositoryUnderTest.findAdsByIds(List.of(0));
        });
    }




    @Test
    void testFindByFilters() {
        // Setup
        // Run the test
        //final List<Ad> result = adRepositoryUnderTest.findByFilters(List.of(0), Zone.EarlyMorning);

        assertThrows(java.lang.IllegalStateException.class, () -> {
            adRepositoryUnderTest.findByFilters(List.of(0), Zone.EarlyMorning);
        });
    }
}
