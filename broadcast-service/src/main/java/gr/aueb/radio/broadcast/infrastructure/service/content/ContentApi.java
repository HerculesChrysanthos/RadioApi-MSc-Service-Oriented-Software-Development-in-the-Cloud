package gr.aueb.radio.broadcast.infrastructure.service.content;

import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
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
    AdBasicRepresentation getAd(
            @HeaderParam("Authorization") String basicAuthHeader,
            @PathParam("id") Integer id
    );

    @GET
    @Path("/songs/{id}")
    SongBasicRepresentation getSongId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @PathParam("id") Integer id
    );
}
