package gr.aueb.radio.content.common;

import gr.aueb.radio.content.common.RadioException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RadioExceptionTest {
    @Test
    public void testConstructorWithMessage() {
        // Given
        String message = "Radio exception occurred";

        // When
        RadioException exception = new RadioException(message);

        // Then
        Assertions.assertEquals(message, exception.getMessage(), "The exception message should match");
        Assertions.assertEquals(0, exception.getStatusCode(), "The status code should be 0");
    }

    @Test
    public void testConstructorWithMessageAndStatusCode() {
        // Given
        String message = "Not found";
        int statusCode = 404;

        // When
        RadioException exception = new RadioException(message, statusCode);

        // Then
        Assertions.assertEquals(message, exception.getMessage(), "The exception message should match");
        Assertions.assertEquals(statusCode, exception.getStatusCode(), "The status code should match");
    }

    @Test
    public void testGetStatusCode() {
        // Given
        int statusCode = 500;
        RadioException exception = new RadioException("Internal Server Error", statusCode);

        // When
        int returnedStatusCode = exception.getStatusCode();

        // Then
        Assertions.assertEquals(statusCode, returnedStatusCode, "The status code should match");
    }

    @Test
    public void testConstructorWithoutMessage() {
        // When
        RadioException exception = new RadioException();

        // Then
        Assertions.assertNull(exception.getMessage(), "The exception message should be null");
        Assertions.assertEquals(0, exception.getStatusCode(), "The status code should be 0");
    }
}
