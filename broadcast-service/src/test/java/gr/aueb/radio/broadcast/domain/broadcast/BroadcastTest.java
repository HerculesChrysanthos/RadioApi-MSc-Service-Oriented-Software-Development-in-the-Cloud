package gr.aueb.radio.broadcast.domain.broadcast;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BroadcastTest {



    Broadcast broadcast;
    private static LocalDate date = DateUtil.setDate("01-01-2023");
    private static LocalTime time = DateUtil.setTime("00:00");
    private static Integer duration = 100;
    @BeforeEach
    public void setUp(){
        broadcast = new Broadcast();
        broadcast.setDuration(duration);
        broadcast.setStartingDate(date);
        broadcast.setStartingTime(time);
        broadcast.setType(BroadcastType.PLAYLIST);

    }

    @Test
    public void successfulSetupTest(){
        assertEquals(duration, broadcast.getDuration());
        assertEquals(date, broadcast.getStartingDate());
        assertEquals(time, broadcast.getStartingTime());
        assertEquals(BroadcastType.PLAYLIST, broadcast.getType());
    }




    @Test
    public void createValidAdBroadcastTest() {

        List<AdBroadcast> broadcasts = broadcast.getAdBroadcasts();
        assertNotNull(broadcasts);
        assertEquals(0, broadcasts.size());
    }

//    @Test
//    public void removeValidAdBroadcastTest() {
//        List<AdBroadcast> broadcasts = broadcast.getAdBroadcasts();
//        assertEquals(0, broadcasts.size());
//        AdBroadcast adBroadcast = broadcasts.get(0);
//        broadcast.removeAdBroadcast(adBroadcast);
//        assertEquals(0, broadcasts.size());
//    }

    @Test
    public void getEndingTimeTest(){
        LocalDateTime expectedTime = date.atTime(time).plusMinutes(duration);
        LocalDateTime endingTime = broadcast.getBroadcastEndingDateTime();
        assertTrue(expectedTime.isEqual(endingTime));
    }
}
