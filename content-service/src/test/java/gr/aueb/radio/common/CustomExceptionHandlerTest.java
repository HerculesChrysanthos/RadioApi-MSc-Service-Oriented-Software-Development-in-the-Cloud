package gr.aueb.radio.common;

import gr.aueb.radio.content.common.CustomExceptionHandler;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomExceptionHandlerTest {
    @Test
    public void testProcessingExceptionHandling() {
        // Instantiate your CustomExceptionHandler
        CustomExceptionHandler exceptionHandler = new CustomExceptionHandler();

        // Create a ProcessingException
        ProcessingException exception = new ProcessingException("Test exception");

        // Invoke the toResponse method with the created exception
        Response response = exceptionHandler.toResponse(exception);

        // Assertions to verify the response status code and message
        Assertions.assertEquals(422, response.getStatus(), "Status code should be 422");

        String expectedJson = "{ \"message\": \"Unsupported data type.\" }";
        Assertions.assertEquals(expectedJson, response.getEntity().toString(), "Error message should match the expected JSON");


    }
}
