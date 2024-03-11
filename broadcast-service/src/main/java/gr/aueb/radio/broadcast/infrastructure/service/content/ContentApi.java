package gr.aueb.radio.broadcast.infrastructure.service.content;

import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.GenreBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey = "content-api")
public interface ContentApi {

    @GET
    @Timeout(10000)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/ads/{id}")
    AdBasicRepresentation getAd(
            @HeaderParam("Authorization") String basicAuthHeader,
            @PathParam("id") Integer id
    );

    @GET
    @Timeout(10000)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/songs/{id}")
    SongBasicRepresentation getSongId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @PathParam("id") Integer id
    );

    @GET
    @Timeout(10000)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/songs")
    List<SongBasicRepresentation> getSongsByFilters(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("artist") String artist,
            @QueryParam("genreId") Integer genreId,
            @QueryParam("genreTitle") String genreTitle,
            @QueryParam("title") String title,
            @QueryParam("songsIds") String songsIds
    );

    @GET
    @Timeout(10000)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/ads")
    List<AdBasicRepresentation> getAdsByFilters(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("timezone") String timezone,
            @QueryParam("adsIds") String adsIds
    );

    @GET
    @Timeout(10000)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/genres")
    List<GenreBasicRepresentation> getAllGenres(
            @HeaderParam("Authorization") String basicAuthHeader
    );

    @GET
    @Timeout(10000)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/genres/{id}")
    GenreBasicRepresentation getGenreById(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String basicAuthHeader
    );
}
