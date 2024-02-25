package gr.aueb.radio.broadcast.infrastructure.service.content;

import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

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

    @GET
    @Path("/songs")
    List<SongBasicRepresentation> getSongsByFilters(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("artist") String artist,
            @QueryParam("genre") String genre,
            @QueryParam("title") String title,
            @QueryParam("songsIds") String songsIds
    );

    @GET
    @Path("/ads")
    List<AdBasicRepresentation> getAdsByFilters(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("timezone") String timezone,
            @QueryParam("adsIds") String adsIds
    );
}
