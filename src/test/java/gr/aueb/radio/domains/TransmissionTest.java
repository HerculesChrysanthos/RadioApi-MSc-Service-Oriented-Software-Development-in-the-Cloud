package gr.aueb.radio.domains;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;

public class TransmissionTest {
    
    Transmission transmission;
    
    private static LocalDateTime localDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 1, 1, 1, 0);

    @BeforeEach
    public void setUp() {
        transmission = new Transmission();
        transmission.setDatetime(localDateTime);
    }
    
    @Test
    public void succefulSetUp() {
        assertEquals(transmission.getDatetime(), localDateTime);
    }
}
