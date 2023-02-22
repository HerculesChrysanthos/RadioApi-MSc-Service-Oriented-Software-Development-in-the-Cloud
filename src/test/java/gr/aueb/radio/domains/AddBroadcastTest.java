package gr.aueb.radio.domains;

import gr.aueb.radio.utils.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class AddBroadcastTest {

    Add add;
    AddBroadcast addBroadcast;

    private static LocalDate broadcastDate = DateUtil.setDate("01-01-2022");

    private static LocalTime broadcastTime = DateUtil.setTime("14:43");


    @BeforeEach
    public void setUp() {

        addBroadcast = new AddBroadcast();

        addBroadcast.setBroadcastDate(broadcastDate);
        addBroadcast.setBroadcastTime(broadcastTime);


    }

    @Test
    public void successfulSetUp() {
        assertEquals(addBroadcast.getBroadcastDate(), broadcastDate);
        assertEquals(addBroadcast.getBroadcastTime(), broadcastTime);
    }

}



