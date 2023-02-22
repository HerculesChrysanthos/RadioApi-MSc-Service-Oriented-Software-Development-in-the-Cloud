package gr.aueb.radio.domains;

import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BroadcastTest {



    Broadcast broadcast;

    @BeforeEach
    public void setUp(){
        broadcast = new Broadcast(100, "01-01-2023", "23:23", BroadcastEnum.NEWS);

    }

    @Test
    public void updateBroadcastTest(){
        String startingDate = "01-01-0001";

        String startingTime = "00:00";
        LocalDate date = DateUtil.setDate(startingDate);
        LocalTime time = DateUtil.setTime(startingTime);

        Integer duration = 100;
        broadcast.setDuration(duration);
        broadcast.setStartingDate(startingDate);
        broadcast.setStartingTime(startingTime);
        broadcast.setType(BroadcastEnum.PLAYLIST);

        Assertions.assertEquals(duration, broadcast.getDuration());
        Assertions.assertEquals(date, broadcast.getStartingDate());
        Assertions.assertEquals(time, broadcast.getStartingTime());
        Assertions.assertEquals(BroadcastEnum.PLAYLIST, broadcast.getType());
    }

    @ParameterizedTest
    @CsvSource({
            "100, 01-01-2023, 23:23, NEWS, true",
            "100, 01-01-2003, 23:23, NEWS, false",
            "100, 01-01-2023, 23:13, NEWS, false",
            "100, 01-01-0001, 00:00, NEWS, false",
    })
    public void equalsBroadcastDataTest(Integer duration, String startingDate, String startingTime, BroadcastEnum broadcastEnum, boolean expectedResult){
        Broadcast broadcastEntity = new Broadcast(duration, startingDate, startingTime, broadcastEnum);
        assertEquals(expectedResult, broadcast.equals(broadcastEntity));
    }

    @Test
    public void equalsObjectTest(){
        assertTrue(broadcast.equals(broadcast));
        assertFalse(broadcast.equals(null));
        assertFalse(broadcast.equals(String.class));
    }
}
