package gr.aueb.radio.common;

import gr.aueb.radio.user.common.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NotFoundExceptionTest {

    @Test
    public void testConstructorWithMessage() {
        String message = "Resource not found";
        NotFoundException exception = new NotFoundException(message);
        Assertions.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructorWithoutMessage() {
        NotFoundException exception = new NotFoundException();
        Assertions.assertNull(exception.getMessage());
    }
}