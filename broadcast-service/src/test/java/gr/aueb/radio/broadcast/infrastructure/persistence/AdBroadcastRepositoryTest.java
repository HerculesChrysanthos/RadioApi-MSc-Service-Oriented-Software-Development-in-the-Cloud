package gr.aueb.radio.broadcast.infrastructure.persistence;

import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

@QuarkusTest
public class AdBroadcastRepositoryTest {

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Test
    void testFfindByTypeDate() {
        final List<AdBroadcast> result = adBroadcastRepository.findByTypeDate(BroadcastType.NEWS, LocalDate.of(2024, 3, 1));
    }
    @Test
    void testFindByDate() {
        final List<AdBroadcast> result = adBroadcastRepository.findByDate(LocalDate.of(2024, 3, 1));
    }

    @Test
    void TestFindByIdDetails() {
        final AdBroadcast result = adBroadcastRepository.findByIdDetails(3001);
    }

    @Test
    void TestFindByAdId() {
        final List<AdBroadcast> result = adBroadcastRepository.findByAdId(3001);;
    }

    @Test
    void TestFfindByFilters() {
        final List<AdBroadcast> result = adBroadcastRepository.findByFilters(LocalDate.of(2024, 3, 1), 3001);;
    }

    @Test
    void TestFfindByDateDetails() {
        final List<AdBroadcast> result = adBroadcastRepository.findByDateDetails(LocalDate.of(2024, 3, 1));;
    }
}