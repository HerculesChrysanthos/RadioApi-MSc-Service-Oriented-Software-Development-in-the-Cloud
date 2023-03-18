package gr.aueb.radio.domains;

import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class AdBroadcastTest {

    Ad ad;
    AdBroadcast adBroadcast;

    Broadcast broadcast;
    private static LocalDate broadcastDate = DateUtil.setDate("01-01-2022");
    private static LocalTime broadcastTime = DateUtil.setTime("14:43");
    private static Integer duration =  29;
    private static Integer repPerZone = 1;
    private static LocalDate startingDate = DateUtil.setDate("01-01-2022");

    private static LocalDate endingDate = DateUtil.setDate("01-03-2022");
    private static ZoneEnum timeZone = ZoneEnum.EarlyMorning;

    @BeforeEach
    public void setUp() {
        adBroadcast = new AdBroadcast();
        adBroadcast.setBroadcastDate(broadcastDate);
        adBroadcast.setBroadcastTime(broadcastTime);
        ad = new Ad(duration, repPerZone, startingDate, endingDate, timeZone);
        broadcast = new Broadcast(100, broadcastDate, broadcastTime, BroadcastEnum.NEWS);
    }

    @Test
    public void successfulSetUp() {
        assertEquals(adBroadcast.getBroadcastDate(), broadcastDate);
        assertEquals(adBroadcast.getBroadcastTime(), broadcastTime);
    }

    @Test
    public void setAdTest(){
        adBroadcast.setAd(ad);
        assertNotNull(adBroadcast.getAd());
    }

    @Test
    public void setBroadcastTest(){
        adBroadcast.setBroadcast(broadcast);
        assertNotNull(adBroadcast.getBroadcast());
    }

    @Test
    public void getEndingTimeTest(){
        adBroadcast.setAd(ad);
        LocalDateTime expectedTime = broadcastDate.atTime(broadcastTime).plusMinutes(duration);
        LocalDateTime broadcastEndingTime = adBroadcast.getBroadcastEndingDateTime();
        assertTrue(expectedTime.isEqual(broadcastEndingTime));
    }

}



