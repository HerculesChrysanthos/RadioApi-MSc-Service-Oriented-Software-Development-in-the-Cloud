package gr.aueb.radio.broadcast.infrastructure.persistence;

import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BroadcastRepositoryTest  implements PanacheRepositoryBase<Broadcast, Integer> {

    private BroadcastRepository broadcastRepositoryUnderTest;

    @BeforeEach
    void setUp() {
        broadcastRepositoryUnderTest = new BroadcastRepository();
    }

    @Test
    void testFindById() {
        // Setup
        // Run the test
        final Broadcast result = broadcastRepositoryUnderTest.findById(3001);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testFindByIdAdDetails() {
        // Setup
        // Run the test
        final Broadcast result = broadcastRepositoryUnderTest.findByIdAdDetails(0);

        // Verify the results
    }

    @Test
    void testFindByIdSongDetails() {
        // Setup
        // Run the test
        final Broadcast result = broadcastRepositoryUnderTest.findByIdSongDetails(0);

        // Verify the results
    }

    @Test
    void testFindByDate() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepositoryUnderTest.findByDate(LocalDate.of(2020, 1, 1));

        // Verify the results
    }

    @Test
    void testFindByDateExcluding() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepositoryUnderTest.findByDateExcluding(LocalDate.of(2020, 1, 1), 0);

        // Verify the results
    }

    @Test
    void testFindByTimeRange() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepositoryUnderTest.findByTimeRange(LocalTime.of(0, 0, 0),
                LocalTime.of(0, 0, 0));

        // Verify the results
    }

    @Test
    void testFindByTimeRangeDate() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepositoryUnderTest.findByTimeRangeDate(LocalTime.of(0, 0, 0),
                LocalTime.of(0, 0, 0), LocalDate.of(2020, 1, 1));

        // Verify the results
    }

    @Test
    void testFindByTimeRangeType() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepositoryUnderTest.findByTimeRangeType(LocalTime.of(0, 0, 0),
                LocalTime.of(0, 0, 0), BroadcastType.NEWS);

        // Verify the results
    }

    @Test
    void testFindByTimeRangeDateType() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepositoryUnderTest.findByTimeRangeDateType(LocalTime.of(0, 0, 0),
                LocalTime.of(0, 0, 0), LocalDate.of(2020, 1, 1), BroadcastType.NEWS);

        // Verify the results
    }
}
