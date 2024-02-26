package gr.aueb.radio.content.infrastructure.persistence;

import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.infrastructure.persistence.AdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class AdRepositoryTest {


    private AdRepository adRepositoryUnderTest;

    @BeforeEach
    void setUp() {
        adRepositoryUnderTest = new AdRepository();
    }

    @Test
    void testFindByTimezone() {
        // Setup
        // Run the test
        final List<Ad> result = adRepositoryUnderTest.findByTimezone(Zone.EarlyMorning);

        // Verify the results
    }

    @Test
    void testFindAdsByIds() {
        // Setup
        // Run the test
        final List<Ad> result = adRepositoryUnderTest.findAdsByIds(List.of(0));

        // Verify the results
    }

    @Test
    void testFindByFilters() {
        // Setup
        // Run the test
        final List<Ad> result = adRepositoryUnderTest.findByFilters(List.of(0), Zone.EarlyMorning);

        // Verify the results
    }
}
