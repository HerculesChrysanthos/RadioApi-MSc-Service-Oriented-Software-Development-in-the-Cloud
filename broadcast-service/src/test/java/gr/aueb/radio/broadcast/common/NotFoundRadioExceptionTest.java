package gr.aueb.radio.broadcast.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class NotFoundRadioExceptionTest {



    @Test
    public void NotFoundRadioExceptionTest() {

        String message = "Resource not found";

        NotFoundRadioException exception = new NotFoundRadioException(message);

        Assertions.assertEquals(message, exception.getMessage(), "The exception message should match");
    }



    @Test
    public void TestNotFoundRadioExceptionSuper() {
        NotFoundRadioException exception = new NotFoundRadioException();
        assertNull(exception.getMessage());
    }


}
