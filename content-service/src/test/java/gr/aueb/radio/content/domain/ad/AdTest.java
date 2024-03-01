package gr.aueb.radio.content.domain.ad;

import gr.aueb.radio.content.common.DateUtil;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class AdTest {

    Ad ad;

    private static Integer duration =  29;
    private static Integer repPerZone = 1;
    private static LocalDate startingDate = DateUtil.setDate("01-01-2022");

    private static LocalDate endingDate = DateUtil.setDate("01-03-2022");
    private static Zone timeZone = Zone.EarlyMorning;

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
    public void testAdCreation() {
        // Arrange
        Integer duration = 30;
        Integer repPerZone = 5;
        LocalDate startingDate = LocalDate.now();
        LocalDate endingDate = startingDate.plusDays(30);
        Zone timezone = Zone.EarlyMorning;

        // Act
        Ad ad = new Ad(duration, repPerZone, startingDate, endingDate, timezone);

        // Assert
        Assertions.assertNotNull(ad);
        Assertions.assertEquals(duration, ad.getDuration());
        Assertions.assertEquals(repPerZone, ad.getRepPerZone());
        Assertions.assertEquals(startingDate, ad.getStartingDate());
        Assertions.assertEquals(endingDate, ad.getEndingDate());
        Assertions.assertEquals(timezone, ad.getTimezone());
    }

    @Test
    public void testAdBroadcasts() {
        // Arrange
        Ad ad = new Ad();
        Integer broadcastId = 1;

        // Act
        ad.addBroadcastAd(broadcastId);

        // Assert
        Assertions.assertEquals(1, ad.getBroadcastAds().size());
        Assertions.assertTrue(ad.getBroadcastAds().contains(broadcastId));
    }

}
