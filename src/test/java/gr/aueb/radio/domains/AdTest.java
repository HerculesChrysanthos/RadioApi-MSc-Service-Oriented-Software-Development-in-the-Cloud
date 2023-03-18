package gr.aueb.radio.domains;

import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AdTest {

    Ad ad;

    private static Integer duration =  29;
    private static Integer repPerZone = 1;
    private static LocalDate startingDate = DateUtil.setDate("01-01-2022");

    private static LocalDate endingDate = DateUtil.setDate("01-03-2022");
    private static ZoneEnum timeZone = ZoneEnum.EarlyMorning;

    private static LocalDate broadcastDate= DateUtil.setDate("01-01-2022");

    private static LocalTime broadcastTime =  DateUtil.setTime("14:43");

    @BeforeEach
    public void setUp() {
        ad = new Ad();
        ad.setDuration(duration);
        ad.setRepPerZone(repPerZone);
        ad.setStartingDate(startingDate);
        ad.setEndingDate(endingDate);
        ad.setTimezone(timeZone);

    }

    @Test
    public void successfulSetUp() {
        assertEquals(ad.getDuration(), duration);
        assertEquals(ad.getRepPerZone(), repPerZone);
        assertEquals(ad.getStartingDate(), startingDate);
        assertEquals(ad.getEndingDate(), endingDate);
        assertEquals(ad.getTimezone(), timeZone);
    }

    @Test
    public void addValidAdBroadcast(){
        AdBroadcast adBroadcast = new AdBroadcast(broadcastDate, broadcastTime);
        ad.addBroadcastAd(adBroadcast);
        List<AdBroadcast> broadcasts = ad.getBroadcastAds();
        assertNotNull(broadcasts);
        assertEquals(1, broadcasts.size());
    }

    @Test
    public void addInvalidAdBroadcast(){
        ad.addBroadcastAd(null);
        List<AdBroadcast> broadcasts = ad.getBroadcastAds();
        assertEquals(0, broadcasts.size());
    }

    @Test
    public void toBeBroadcastedTest(){
        AdBroadcast adBroadcast = new AdBroadcast(broadcastDate, broadcastTime);
        assertTrue(ad.toBeBroadcasted(broadcastDate));
        ad.addBroadcastAd(adBroadcast);
        assertFalse(ad.toBeBroadcasted(broadcastDate));
    }

    @Test
    public void removeAdBroadcast(){
        AdBroadcast adBroadcast = new AdBroadcast(broadcastDate, broadcastTime);
        ad.addBroadcastAd(adBroadcast);
        List<AdBroadcast> broadcasts = ad.getBroadcastAds();
        assertEquals(1, broadcasts.size());
        ad.removeAdBroadcast(adBroadcast);
        broadcasts = ad.getBroadcastAds();
        assertEquals(0, broadcasts.size());
    }

}







