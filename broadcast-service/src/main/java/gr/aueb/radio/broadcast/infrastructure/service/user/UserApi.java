package gr.aueb.radio.broadcast.infrastructure.service.user;

import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

@Path("/api/users")
@ApplicationScoped
@RegisterRestClient(configKey="user-api")
public interface UserApi {

    @GET
    @Retry(maxRetries = 3, delay = 2, delayUnit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/verify-auth")
    UserVerifiedRepresentation verifyAuth(
            @HeaderParam("Authorization") String basicAuthHeader
    );
}
