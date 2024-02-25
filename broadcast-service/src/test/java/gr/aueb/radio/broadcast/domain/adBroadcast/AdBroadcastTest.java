package gr.aueb.radio.broadcast.domain.adBroadcast;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class AdBroadcastTest {


    AdBroadcast adBroadcast;

    Broadcast broadcast;
    private static LocalDate broadcastDate = DateUtil.setDate("01-01-2022");
    private static LocalTime broadcastTime = DateUtil.setTime("14:43");
    private static Integer duration =  29;
    private static Integer repPerZone = 1;
    private static LocalDate startingDate = DateUtil.setDate("01-01-2022");

    private static LocalDate endingDate = DateUtil.setDate("01-03-2022");


    @BeforeEach
    public void setUp() {
        adBroadcast = new AdBroadcast();
        adBroadcast.setBroadcastDate(broadcastDate);
        adBroadcast.setBroadcastTime(broadcastTime);
        broadcast = new Broadcast();
    }

    @Test
    public void testAdBroadcastConstructor() {
        // Arrange
        LocalDate expectedDate = LocalDate.of(2024, 2, 25);
        LocalTime expectedTime = LocalTime.of(12, 30); // for example

        // Act
        AdBroadcast adBroadcast = new AdBroadcast(expectedDate, expectedTime);

        // Assert
        assertEquals(expectedDate, adBroadcast.getBroadcastDate());
        assertEquals(expectedTime, adBroadcast.getBroadcastTime());
    }

    @Test
    public void successfulSetUp() {
        assertEquals(adBroadcast.getBroadcastDate(), broadcastDate);
        assertEquals(adBroadcast.getBroadcastTime(), broadcastTime);
    }


    @Test
    public void setBroadcastTest(){
        adBroadcast.setBroadcast(broadcast);
        assertNotNull(adBroadcast.getBroadcast());
    }

    @Test
    public void getEndingTimeTest(){
        LocalDateTime expectedTime = broadcastDate.atTime(broadcastTime).plusMinutes(duration);
        LocalDateTime broadcastEndingTime = adBroadcast.getBroadcastEndingDateTime(12);
        assertFalse(expectedTime.isEqual(broadcastEndingTime));
    }

    @Test
    public void testGetId() {
        // Arrange

        AdBroadcast adBroadcast = new AdBroadcast();


        // Act
        Integer actualId = adBroadcast.getId();

        // Assert
        assertEquals(null, actualId);
    }

    @Test
    public void testGetBroadcast() {
        // Arrange
        Broadcast expectedBroadcast = new Broadcast(); // assuming Broadcast is another class
        AdBroadcast adBroadcast = new AdBroadcast();
        adBroadcast.setBroadcast(expectedBroadcast);

        // Act
        Broadcast actualBroadcast = adBroadcast.getBroadcast();

        // Assert
        assertEquals(expectedBroadcast, actualBroadcast);
    }

    @Test
    public void testGetId_Null() {
        // Arrange
        AdBroadcast adBroadcast = new AdBroadcast();

        // Act
        Integer actualId = adBroadcast.getId();

        // Assert
        assertNull(actualId);
    }

    @Test
    public void testGetBroadcast_Null() {
        // Arrange
        AdBroadcast adBroadcast = new AdBroadcast();

        // Act
        Broadcast actualBroadcast = adBroadcast.getBroadcast();

        // Assert
        assertNull(actualBroadcast);
    }

}



