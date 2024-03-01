package gr.aueb.radio.broadcast.domain.songBroadcast;

import gr.aueb.radio.broadcast.infrastructure.persistence.SongBroadcastRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
@QuarkusTest
class SongBroadcastTest {
    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Test
    public void testGetBroadcastEndingDateTime (){
     SongBroadcast sb = songBroadcastRepository.findById(5001);
        LocalDate broadcastDate = sb.getBroadcastDate();
        LocalTime broadcastTime = sb.getBroadcastTime();

        int duration = 60;

        // Calculate the expected ending date time
        LocalDateTime startingDateTime = LocalDateTime.of(broadcastDate, broadcastTime);
        LocalDateTime expectedEndingDateTime = startingDateTime.plusMinutes(duration);

        LocalDateTime actualEndingDateTime = sb.getBroadcastEndingDateTime(duration);
        assertEquals(expectedEndingDateTime, actualEndingDateTime);

    }

    @Test
    void testConstructor() {
        LocalDate broadcastDate = LocalDate.of(2024, 3, 1);
        LocalTime broadcastTime = LocalTime.of(8, 30);

        SongBroadcast songBroadcast = new SongBroadcast(broadcastDate, broadcastTime);
        assertEquals(broadcastDate, songBroadcast.getBroadcastDate());
        assertEquals(broadcastTime, songBroadcast.getBroadcastTime());
        assertEquals(null, songBroadcast.getSongId()); // Ensure songId is null
    }
    @Test
    void testConstructorWithSongId() {
        LocalDate broadcastDate = LocalDate.of(2024, 3, 1);
        LocalTime broadcastTime = LocalTime.of(8, 30);
        int songId = 7012;

        SongBroadcast songBroadcast = new SongBroadcast(broadcastDate, broadcastTime, songId);

        assertEquals(broadcastDate, songBroadcast.getBroadcastDate());
        assertEquals(broadcastTime, songBroadcast.getBroadcastTime());
        assertEquals(songId, songBroadcast.getSongId());
    }
}
