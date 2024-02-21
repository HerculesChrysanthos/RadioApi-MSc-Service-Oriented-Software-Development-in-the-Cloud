package gr.aueb.radio.broadcast.infrastructure.service.content;

import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey="content-api")
public interface ContentApi {

    @GET
    @Path("/ads/{id}")
    AdBasicRepresentation getAdId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @PathParam("id") Integer id
    );
}
