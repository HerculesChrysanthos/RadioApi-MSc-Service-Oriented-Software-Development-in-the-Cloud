package gr.aueb.radio.broadcast.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorResponseTest {

    @Test
    public void testErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getAcceptedValues());
    }

    @Test
    public void testErrorResponseWithMessage() {
        String message = "Error message";
        ErrorResponse errorResponse = new ErrorResponse(message);
        assertEquals(message, errorResponse.getMessage());
        assertNull(errorResponse.getAcceptedValues());
    }

    @Test
    public void testErrorResponseWithMessageAndAcceptedValues() {
        String message = "Error message";
        String[] acceptedValues = {"value1", "value2"};
        ErrorResponse errorResponse = new ErrorResponse(message, acceptedValues);
        assertEquals(message, errorResponse.getMessage());
        assertArrayEquals(acceptedValues, errorResponse.getAcceptedValues());
    }

    @Test
    public void testErrorResponseDef() {
        ErrorResponse errorResponse = new ErrorResponse();
        String message = "Error message";
        errorResponse.setMessage(message);
        assertEquals(message, errorResponse.getMessage());
    }

    @Test
    public void testSetAcceptedValues() {
        ErrorResponse errorResponse = new ErrorResponse();
        String[] acceptedValues = {"junitvalue1", "junitvalue2"};
        errorResponse.setAcceptedValues(acceptedValues);
        assertArrayEquals(acceptedValues, errorResponse.getAcceptedValues());
    }
}
