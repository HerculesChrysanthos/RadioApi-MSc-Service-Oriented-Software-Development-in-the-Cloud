package gr.aueb.radio.content.infrastructure.service.user;

import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

@Path("/api/users")
@ApplicationScoped
@RegisterRestClient(configKey="user-api")
public interface UserApi {

    @GET
    @Path("/verify-auth")
    @Timeout(500)
    @Retry(maxRetries = 4, delay = 1000,
            delayUnit = ChronoUnit.MILLIS)
    UserVerifiedRepresentation verifyAuth(
            @HeaderParam("Authorization") String basicAuthHeader
    );
}
