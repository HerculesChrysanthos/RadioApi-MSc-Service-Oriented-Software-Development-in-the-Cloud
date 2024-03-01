package gr.aueb.radio.content.common;

import gr.aueb.radio.content.common.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {


    @Test
    public void testConstructor() {
        String errorMessage = "Test error message";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        assertEquals(errorMessage, errorResponse.getMessage());
    }

    @Test
    public void testSetMessage() {
        ErrorResponse errorResponse = new ErrorResponse();
        String errorMessage = "Test error message";
        errorResponse.setMessage(errorMessage);
        assertEquals(errorMessage, errorResponse.getMessage());
    }

}