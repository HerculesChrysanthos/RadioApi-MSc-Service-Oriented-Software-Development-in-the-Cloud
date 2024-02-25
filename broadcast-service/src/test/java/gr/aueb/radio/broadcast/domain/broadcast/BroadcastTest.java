package gr.aueb.radio.broadcast.domain.broadcast;

import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BroadcastTest {

    @Test
    public void testCreateAdBroadcast() {
        // Arrange
        Broadcast broadcast = new Broadcast(60, LocalDate.of(2024, 2, 25), LocalTime.of(12, 0), BroadcastType.PLAYLIST);

        // Act
        AdBroadcast adBroadcast = broadcast.createAdBroadcast(new AdBasicRepresentation(), LocalTime.of(12, 30), new ArrayList<>());

        // Assert
        assertNotNull(adBroadcast);
        assertEquals(broadcast, adBroadcast.getBroadcast());
    }

    @Test
    public void testCreateSongBroadcast() {
        // Arrange
        Broadcast broadcast = new Broadcast(60, LocalDate.of(2024, 2, 25), LocalTime.of(12, 0), BroadcastType.PLAYLIST);

        // Act
        SongBroadcast songBroadcast = broadcast.createSongBroadcast(new SongBasicRepresentation(), LocalTime.of(12, 30), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // Assert
        assertNotNull(songBroadcast);
        assertEquals(broadcast, songBroadcast.getBroadcast());
    }

    @Test
    public void testRemoveAdBroadcast() {
        // Arrange
        Broadcast broadcast = new Broadcast();
        AdBroadcast adBroadcast = new AdBroadcast();
        broadcast.getAdBroadcasts().add(adBroadcast);

        // Act
        broadcast.removeAdBroadcast(adBroadcast);

        // Assert
        assertTrue(broadcast.getAdBroadcasts().isEmpty());
        assertNull(adBroadcast.getBroadcast());
    }

    @Test
    public void testRemoveSongBroadcast() {
        // Arrange
        Broadcast broadcast = new Broadcast();
        SongBroadcast songBroadcast = new SongBroadcast();
        broadcast.getSongBroadcasts().add(songBroadcast);

        // Act
        broadcast.removeSongBroadcast(songBroadcast);

        // Assert
        assertTrue(broadcast.getSongBroadcasts().isEmpty());
        assertNull(songBroadcast.getBroadcast());
    }

    // Add more test cases to cover other methods and edge cases
}
