package gr.aueb.radio.content.common;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<ProcessingException> {

    @Override
    public Response toResponse(ProcessingException e) {
        e.printStackTrace();
        String jsonErrorMessage = "{ \"message\": \"Unsupported data type.\" }";
        return Response.status(422).entity(jsonErrorMessage).build();
    }
}