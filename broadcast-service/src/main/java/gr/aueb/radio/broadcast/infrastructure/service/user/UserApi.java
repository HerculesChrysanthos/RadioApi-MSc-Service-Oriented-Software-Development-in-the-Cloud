package gr.aueb.radio.broadcast.infrastructure.service.user;

import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/users")
@ApplicationScoped
@RegisterRestClient(configKey="user-api")
public interface UserApi {

    @GET
    @Path("/verify-auth")
    UserVerifiedRepresentation verifyAuth(
            @HeaderParam("Authorization") String basicAuthHeader
    );
}
