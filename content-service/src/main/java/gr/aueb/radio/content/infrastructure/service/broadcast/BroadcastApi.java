package gr.aueb.radio.content.infrastructure.service.broadcast;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

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
}
