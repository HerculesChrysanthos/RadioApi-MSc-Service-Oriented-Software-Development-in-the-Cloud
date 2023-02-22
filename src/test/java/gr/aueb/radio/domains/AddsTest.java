package gr.aueb.radio.domains;

import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AddsTest {

    Add add;

    private static Integer duration =  29;
    private static Integer repPerZone = 1;
    private static LocalDate startingDate = DateUtil.setDate("01-01-2022");

    private static LocalDate endingDate = DateUtil.setDate("01-03-2022");
    private static ZoneEnum timeZone = ZoneEnum.EarlyMorning;

    private static LocalDate broadcastDate= DateUtil.setDate("01-01-2022");

    private static LocalTime broadcastTime =  DateUtil.setTime("14:43");

    @BeforeEach
    public void setUp() {
        add = new Add();
        add.setDuration(duration);
        add.setRepPerZone(repPerZone);
        add.setStartingDate(startingDate);
        add.setEndingDate(endingDate);
        add.setTimezone(timeZone);

    }

    @Test
    public void successfulSetUp() {
        assertEquals(add.getDuration(), duration);
        assertEquals(add.getRepPerZone(), repPerZone);
        assertEquals(add.getStartingDate(), startingDate);
        assertEquals(add.getEndingDate(), endingDate);
        assertEquals(add.getTimezone(), timeZone);
    }

    @Test
    public void addValidAddBroadcast(){
        AddBroadcast addBroadcast = new AddBroadcast(broadcastDate, broadcastTime);
        add.addBroadcastAdd(addBroadcast);
        List<AddBroadcast> broadcasts = add.getBroadcastAdds();
        assertNotNull(broadcasts);
        assertEquals(1, broadcasts.size());
    }

    @Test
    public void addInvalidAddBroadcast(){
        add.addBroadcastAdd(null);
        List<AddBroadcast> broadcasts = add.getBroadcastAdds();
        assertEquals(0, broadcasts.size());
    }


    @Test
    public void equalsObjectTest(){
        Assertions.assertTrue(add.equals(add));
        assertFalse(add.equals(null));
        assertFalse(add.equals(String.class));
    }

}







