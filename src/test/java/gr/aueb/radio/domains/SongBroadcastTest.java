package gr.aueb.radio.domains;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;

public class SongBroadcastTest {
    
    SongBroadcast songBroadcast;
    
    private static LocalDate localDate = LocalDate.of(2023, Month.FEBRUARY, 1);
    private static LocalTime localTime = LocalTime.of(1, 0);

    @BeforeEach
    public void setUp() {
        songBroadcast = new SongBroadcast();
        songBroadcast.setBroadcastDate(localDate);
        songBroadcast.setBroadcastTime(localTime);
    }
    
    @Test
    public void successfulSetUp() {
        assertEquals(songBroadcast.getBroadcastDate(), localDate);
        assertEquals(songBroadcast.getBroadcastTime(), localTime);
    }
}
