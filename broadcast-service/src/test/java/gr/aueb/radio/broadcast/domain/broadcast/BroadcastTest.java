package gr.aueb.radio.broadcast.domain.broadcast;

import gr.aueb.radio.broadcast.application.AdBroadcastService;
import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BroadcastTest {



    Broadcast broadcast;
    private static LocalDate date = DateUtil.setDate("01-01-2023");
    private static LocalTime time = DateUtil.setTime("13:14");
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

    @Test
    public void toBeBroadcastedSongTest()
    {

        Broadcast broadcast=new Broadcast();
        List<AdBroadcast> broadcasts = broadcast.getAdBroadcasts();
        System.out.println("Size of broadcasts list: " + broadcasts.size());
        boolean result=broadcast.toBeBroadcastedSong(date,time,broadcasts);
        assertTrue(result);

    }

    @Test
    public  void toBeBroadcastedAdTest()
    {
        String dateString = "01-02-2022";
        String dateString_2 = "02-02-2022";

        // Define the pattern
        String pattern = "dd-MM-yyyy";

        // Create a DateTimeFormatter with the specified pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Parse the date string into a LocalDate object
        LocalDate date_1 = LocalDate.parse(dateString, formatter);
        LocalDate date_2 = LocalDate.parse(dateString_2, formatter);
        Broadcast broadcast=new Broadcast();
        AdBasicRepresentation adBasicRepresentation=new AdBasicRepresentation();
        adBasicRepresentation.startingDate=date_1;
        adBasicRepresentation.endingDate=date_2;
        List<AdBroadcast> broadcasts = broadcast.getAdBroadcasts();
        boolean result=broadcast.toBeBroadcastedAd(date,adBasicRepresentation,broadcasts);
        assertFalse(result);
    }

    @Test
    public void createAdBroadcast()
    {
        String dateString = "01-02-2022";
        String dateString_2 = "02-02-2022";

        // Define the pattern
        String pattern = "dd-MM-yyyy";

        // Create a DateTimeFormatter with the specified pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Parse the date string into a LocalDate object
        LocalDate date_1 = LocalDate.parse(dateString, formatter);
        LocalDate date_2 = LocalDate.parse(dateString_2, formatter);

        AdBasicRepresentation adBasicRepresentation=new AdBasicRepresentation();
        adBasicRepresentation.id=1;
        adBasicRepresentation.timezone= Zone.EarlyMorning.name();
        adBasicRepresentation.startingDate=date_1;
        adBasicRepresentation.endingDate=date_2;
        adBasicRepresentation.duration=200;
        List<AdBroadcast> broadcasts = broadcast.getAdBroadcasts();
        List<AdBasicRepresentation> adBasicRepresentationlist = new ArrayList<>();
        adBasicRepresentationlist.add(adBasicRepresentation);

        Broadcast broadcast=new Broadcast();
        broadcast.setStartingTime(time);

        AdBroadcast result=broadcast.createAdBroadcast(adBasicRepresentation,time,broadcasts,adBasicRepresentationlist);

        assertNull(result);

    }

    @Test
    public void adCanBeAdded()
    {
        String dateString = "01-02-2022";
        String dateString_2 = "02-02-2022";

        // Define the pattern
        String pattern = "dd-MM-yyyy";

        // Create a DateTimeFormatter with the specified pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Parse the date string into a LocalDate object
        LocalDate date_1 = LocalDate.parse(dateString, formatter);
        LocalDate date_2 = LocalDate.parse(dateString_2, formatter);

        AdBasicRepresentation adBasicRepresentation=new AdBasicRepresentation();
        adBasicRepresentation.id=1;
        adBasicRepresentation.timezone= Zone.EarlyMorning.name();
        adBasicRepresentation.startingDate=date_1;
        adBasicRepresentation.endingDate=date_2;
        adBasicRepresentation.duration=200;
        List<AdBroadcast> broadcasts = broadcast.getAdBroadcasts();
        List<AdBasicRepresentation> adBasicRepresentationlist = new ArrayList<>();
        adBasicRepresentationlist.add(adBasicRepresentation);

        Broadcast broadcast=new Broadcast();
        broadcast.setStartingTime(time);

        boolean result=broadcast.adCanBeAdded(adBasicRepresentation,time,broadcasts,adBasicRepresentationlist);
        assertFalse(result);
    }

    @Test
    public void Broadcast()
    {
        broadcast = new Broadcast(duration,date,time,BroadcastType.PLAYLIST);
        broadcast.setDuration(duration);
        broadcast.setStartingDate(date);
        broadcast.setStartingTime(time);
        broadcast.setType(BroadcastType.PLAYLIST);
    }


}
