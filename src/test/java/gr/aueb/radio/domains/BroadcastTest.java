package gr.aueb.radio.domains;

import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
        broadcast.setType(BroadcastEnum.PLAYLIST);

    }

    @Test
    public void successfulSetupTest(){
        assertEquals(duration, broadcast.getDuration());
        assertEquals(date, broadcast.getStartingDate());
        assertEquals(time, broadcast.getStartingTime());
        assertEquals(BroadcastEnum.PLAYLIST, broadcast.getType());
    }
}
