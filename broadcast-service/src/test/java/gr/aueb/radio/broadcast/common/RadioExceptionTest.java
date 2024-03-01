package gr.aueb.radio.broadcast.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RadioExceptionTest {


    @Test
    public void testDefaultConstructor() {
        RadioException exception = new RadioException();
        assertNull(exception.getMessage());
        assertEquals(0, exception.getStatusCode());
    }
}
