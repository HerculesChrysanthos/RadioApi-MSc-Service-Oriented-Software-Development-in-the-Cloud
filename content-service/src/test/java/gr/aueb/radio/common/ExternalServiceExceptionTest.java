package gr.aueb.radio.common;

import gr.aueb.radio.content.common.ExternalServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ExternalServiceExceptionTest {

    @Test
    public void testDefaultConstructor() {
        ExternalServiceException exception = new ExternalServiceException();
        assertEquals(424, exception.getStatusCode());
        assertNull(exception.getMessage());
    }

    @Test
    public void testMessage() {
        String message = "Failed Dependency";
        ExternalServiceException exception = new ExternalServiceException(message);
        assertEquals(424, exception.getStatusCode());
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageAndStatusCodeConstructor() {
        String message = "Custom message";
        int statusCode = 500;
        ExternalServiceException exception = new ExternalServiceException(message, statusCode);
        assertEquals(statusCode, exception.getStatusCode());
        assertEquals(message, exception.getMessage());
    }
}
