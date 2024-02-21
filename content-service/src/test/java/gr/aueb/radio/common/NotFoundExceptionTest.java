package gr.aueb.radio.common;

import gr.aueb.radio.content.common.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NotFoundExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        // Given
        String message = "Resource not found";

        // When
        NotFoundException exception = new NotFoundException(message);

        // Then
        Assertions.assertEquals(message, exception.getMessage(), "The exception message should match");
    }

    @Test
    public void testConstructorWithoutMessage() {
        // When
        NotFoundException exception = new NotFoundException();

        // Then
        Assertions.assertNull(exception.getMessage(), "The exception message should be null");
    }
}
