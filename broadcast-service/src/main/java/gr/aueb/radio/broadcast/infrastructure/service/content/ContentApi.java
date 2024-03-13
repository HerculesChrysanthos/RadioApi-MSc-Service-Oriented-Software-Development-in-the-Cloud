package gr.aueb.radio.broadcast.infrastructure.service.content;

import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.GenreBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey = "content-api")
public interface ContentApi {

    @GET
    @Retry(maxRetries = 4, delay = 1000, maxDuration = 5000, delayUnit = ChronoUnit.MILLIS)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/ads/{id}")
    AdBasicRepresentation getAd(
            @HeaderParam("Authorization") String basicAuthHeader,
            @PathParam("id") Integer id
    );

    @GET
    @Retry(maxRetries = 4, delay = 1000, maxDuration = 5000, delayUnit = ChronoUnit.MILLIS)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/songs/{id}")
    SongBasicRepresentation getSongId(
            @HeaderParam("Authorization") String basicAuthHeader,
            @PathParam("id") Integer id
    );

    @GET
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
    @Retry(maxRetries = 4, delay = 1000, maxDuration = 5000, delayUnit = ChronoUnit.MILLIS)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/ads")
    List<AdBasicRepresentation> getAdsByFilters(
            @HeaderParam("Authorization") String basicAuthHeader,
            @QueryParam("timezone") String timezone,
            @QueryParam("adsIds") String adsIds
    );

    @GET
    @Retry(maxRetries = 4, delay = 1000, maxDuration = 5000, delayUnit = ChronoUnit.MILLIS)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/genres")
    List<GenreBasicRepresentation> getAllGenres(
            @HeaderParam("Authorization") String basicAuthHeader
    );

    @GET
    @Retry(maxRetries = 4, delay = 1000, maxDuration = 5000, delayUnit = ChronoUnit.MILLIS)
    @CircuitBreaker(requestVolumeThreshold = 3, delay = 10000, successThreshold = 2)
    @Path("/genres/{id}")
    GenreBasicRepresentation getGenreById(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String basicAuthHeader
    );
}
