package gr.aueb.radio.domains;

import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class AddsTest {

    Add add;
    BroadcastAdd broadcastAdd;

    private static Integer duration =  29;
    private static Integer repPerZone = 1;
    private static LocalDate startingDate = DateUtil.setDate("01-01-2022");

    private static LocalDate endingDate = DateUtil.setDate("01-03-2022");
    private static ZoneEnum TimeZone = ZoneEnum.EarlyMorning;

    private static LocalDate broadcastDate= DateUtil.setDate("01-01-2022");

    private static LocalTime broadcastTime =  DateUtil.setTime("14:43");

    @BeforeEach
    public void setUp() {
        broadcastAdd = new BroadcastAdd();
        add = new Add();

        broadcastAdd.setBroadcastDate(broadcastDate);
        broadcastAdd.setBroadcastTime(broadcastTime);
        add.setDuration(duration);
        add.setRepPerZone(repPerZone);
        add.setStartingDate(startingDate);
        add.setEndingDate(endingDate);
        add.setTimeZone(TimeZone);

    }

    @Test
    public void successfulSetUp() {
        assertEquals(add.getDuration(), duration);
        assertEquals(add.getRepPerZone(), repPerZone);
        assertEquals(add.getStartingDate(), startingDate);
        assertEquals(add.getEndingDate(), endingDate);
        assertEquals(add.getTimeZone(), TimeZone);
    }

//    @Test
//    public void checkRepsPerZone () {
//        to be added
//    }
    @Test
    public void addingSameBroadcastaddInTwoAdds() {
        Add add3 = new Add();
        add3.addBroadcastadd(broadcastAdd);
        broadcastAddComp(add);
        broadcastAddComp(add3);
    }


    private void broadcastAddComp(Add add) {
        for(BroadcastAdd  broadcastAdd: add.getBroadcastAdds()) {
            Assertions.assertSame(  add, broadcastAdd.getAdd());
        }
    }

}







