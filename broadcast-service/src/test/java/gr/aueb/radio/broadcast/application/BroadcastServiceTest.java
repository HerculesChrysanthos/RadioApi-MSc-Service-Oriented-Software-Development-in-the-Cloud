package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class BroadcastServiceTest {

    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    BroadcastService broadcastService;

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Test
    void testRemoveAdBr() {
        Broadcast broadcast = broadcastRepository.findByIdAdDetails(3001);
        broadcastService.removeAdBroadcast(broadcast.getId(), 4001);

        Integer abId = broadcast.getAdBroadcasts().get(0).getId();
        AdBroadcast ab = adBroadcastRepository.findById(abId);
        Integer numOfAbs = broadcast.getAdBroadcasts().size();
//        broadcastService.removeAdBroadcast(broadcast.getId(), 1001);

        broadcast.removeAdBroadcast(ab);

        assertEquals(numOfAbs - 1, broadcast.getAdBroadcasts().size());
    }


}
