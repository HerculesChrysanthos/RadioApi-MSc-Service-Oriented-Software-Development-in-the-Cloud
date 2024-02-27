package gr.aueb.radio.content.infrastructure.service.broadcast;

import gr.aueb.radio.content.infrastructure.service.broadcast.representation.AdBroadcastBasicRepresentation;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.SongBroadcastBasicRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey="broadcast-api")
public interface BroadcastApi {

    @DELETE
    @Path("/song-broadcasts/")
    Response deleteSongBroadcastsBySongId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("songId") Integer songId
    );

    @DELETE
    @Path("/ad-broadcasts/")
    Response deleteAdBroadcastsByAdId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("adId") Integer adId
    );

    @GET
    @Path("/song-broadcasts")
    List<SongBroadcastBasicRepresentation> getSongBroadcastsBySongId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("songId") Integer songId
    );

    @GET
    @Path("/ad-broadcasts")
    List<AdBroadcastBasicRepresentation> getAdBroadcastsByAdId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("adId") Integer adId
    );
}
