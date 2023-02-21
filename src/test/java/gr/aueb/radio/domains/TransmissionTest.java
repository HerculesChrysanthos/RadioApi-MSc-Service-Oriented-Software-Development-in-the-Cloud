package gr.aueb.radio.domains;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;

public class TransmissionTest {
    
    Transmission transmission;
    
    private static LocalDate localDate = LocalDate.of(2023, Month.FEBRUARY, 1);
    private static LocalTime localTime = LocalTime.of(1, 0);

    @BeforeEach
    public void setUp() {
        transmission = new Transmission();
        transmission.setDate(localDate);
        transmission.setTime(localTime);
    }
    
    @Test
    public void succefulSetUp() {
        assertEquals(transmission.getDate(), localDate);
        assertEquals(transmission.getTime(), localTime);
    }
}
