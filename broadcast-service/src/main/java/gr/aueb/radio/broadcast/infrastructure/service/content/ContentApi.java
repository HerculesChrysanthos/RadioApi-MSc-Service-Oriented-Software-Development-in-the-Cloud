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
import org.jboss.logging.Logger;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey = "content-api")
public interface ContentApi {

    @GET
    @Retry(maxRetries = 3, delay = 5000)
    @Path("/ads/{id}")
    AdBasicRepresentation getAd(
            @HeaderParam("Authorization") String basicAuthHeader,
            @PathParam("id")
            Integer id
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
            @QueryParam("genreId") Integer genreId,
            @QueryParam("genreTitle") String genreTitle,
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

    @GET
    @Path("/genres")
    List<GenreBasicRepresentation> getAllGenres(
            @HeaderParam("Authorization") String basicAuthHeader
    );

    @GET
    @Path("/genres/{id}")
    GenreBasicRepresentation getGenreById(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String basicAuthHeader
    );
}
