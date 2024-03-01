package gr.aueb.radio.broadcast.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ExternalServiceExceptionTest {

    @Test
    public void testDefaultExternalServiceException() {
        ExternalServiceException exception = new ExternalServiceException();
        assertNull(exception.getMessage());
        assertEquals(424, exception.getStatusCode());
    }

    @Test
    public void testExternalServiceExceptionWithMessage() {
        String message = "External service error";
        ExternalServiceException exception = new ExternalServiceException(message);
        assertEquals(message, exception.getMessage());
        assertEquals(424, exception.getStatusCode());
    }

    @Test
    public void testExternalServiceExceptionWithMessageAndStatusCode() {
        String message = "External service error";
        int statusCode = 500;
        ExternalServiceException exception = new ExternalServiceException(message, statusCode);
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
    }
}
