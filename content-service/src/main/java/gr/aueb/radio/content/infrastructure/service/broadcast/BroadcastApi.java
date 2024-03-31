package gr.aueb.radio.content.infrastructure.service.broadcast;

import gr.aueb.radio.content.infrastructure.service.broadcast.representation.AdBroadcastBasicRepresentation;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.SongBroadcastBasicRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.List;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey="broadcast-api")
//@Timeout(5000)
public interface BroadcastApi {

    @DELETE
    @Path("/song-broadcasts")
    Response deleteSongBroadcastsBySongId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("songId") Integer songId
    );

    @DELETE
    @Path("/ad-broadcasts")
    @Retry(maxRetries = 3)
    Response deleteAdBroadcastsByAdId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("adId") Integer adId
    );


    @GET
//    @Retry(maxRetries = 4, delay = 3000)
    @Path("/song-broadcasts")
    List<SongBroadcastBasicRepresentation> getSongBroadcastsBySongId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("songId") Integer songId
    );

    @GET
    @Retry(maxRetries = 1, delay = 5000)
    @Path("/ad-broadcasts")
    List<AdBroadcastBasicRepresentation> getAdBroadcastsByAdId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("adId") Integer adId
    );
}
