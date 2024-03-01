package gr.aueb.radio.broadcast.infrastructure.persistence;

import gr.aueb.radio.broadcast.common.IntegrationBase;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class BroadcastRepositoryTest extends IntegrationBase {
    @Inject
    BroadcastRepository broadcastRepository;

    @Test
    void testFindById() {
        Broadcast result = broadcastRepository.findById(3001);
        assertNotNull(result);
        assertEquals(3001, result.getId());
    }

    @Test
    void testFindByIdAdDetails() {
        final Broadcast result = broadcastRepository.findByIdAdDetails(0);
    }

    @Test
    void testFindByIdSongDetails() {
        final Broadcast result = broadcastRepository.findByIdSongDetails(0);
    }

    @Test
    void testFindByDate() {
        final List<Broadcast> result = broadcastRepository.findByDate(LocalDate.of(2020, 1, 1));
    }

    @Test
    void testFindByDateExcluding() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepository.findByDateExcluding(LocalDate.of(2020, 1, 1), 0);

        // Verify the results
    }

    @Test
    void testFindByTimeRange() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepository.findByTimeRange(LocalTime.of(0, 0, 0),
                LocalTime.of(0, 0, 0));

        // Verify the results
    }

    @Test
    void testFindByTimeRangeDate() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepository.findByTimeRangeDate(LocalTime.of(0, 0, 0),
                LocalTime.of(0, 0, 0), LocalDate.of(2020, 1, 1));

        // Verify the results
    }

    @Test
    void testFindByTimeRangeType() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepository.findByTimeRangeType(LocalTime.of(0, 0, 0),
                LocalTime.of(0, 0, 0), BroadcastType.NEWS);

        // Verify the results
    }

    @Test
    void testFindByTimeRangeDateType() {
        // Setup
        // Run the test
        final List<Broadcast> result = broadcastRepository.findByTimeRangeDateType(LocalTime.of(0, 0, 0),
                LocalTime.of(0, 0, 0), LocalDate.of(2020, 1, 1), BroadcastType.NEWS);

        // Verify the results
    }
}
