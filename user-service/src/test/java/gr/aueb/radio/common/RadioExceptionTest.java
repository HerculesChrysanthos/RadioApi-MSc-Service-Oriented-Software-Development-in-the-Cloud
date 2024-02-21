package gr.aueb.radio.common;

import gr.aueb.radio.user.common.RadioException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RadioExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "Radio exception occurred";
        RadioException exception = new RadioException(message);
        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(0, exception.getStatusCode()); // Default status code
    }

    @Test
    public void testConstructorWithMessageAndStatusCode() {
        String message = "Not found";
        int statusCode = 404;
        RadioException exception = new RadioException(message, statusCode);
        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(statusCode, exception.getStatusCode());
    }

    @Test
    public void testConstructorWithoutMessage() {
        RadioException exception = new RadioException();
        Assertions.assertNull(exception.getMessage());
        Assertions.assertEquals(0, exception.getStatusCode()); // Default status code
    }
}