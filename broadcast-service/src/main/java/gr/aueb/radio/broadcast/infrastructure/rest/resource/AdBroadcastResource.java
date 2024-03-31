package gr.aueb.radio.broadcast.infrastructure.rest.resource;

import gr.aueb.radio.broadcast.application.AdBroadcastService;
import gr.aueb.radio.broadcast.common.ErrorResponse;
import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.infrastructure.rest.ApiPath.Root;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdBroadcastCreationDTO;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdBroadcastMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.faulttolerance.*;
import org.jboss.logging.Logger;


import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Path(Root.AD_BROADCASTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AdBroadcastResource {

    @Inject
    AdBroadcastService adBroadcastService;
    @Inject
    AdBroadcastMapper adBroadcastMapper;

    @Context
    UriInfo uriInfo;
    private static final Logger LOGGER = Logger.getLogger(AdBroadcastResource.class);
    private AtomicLong counter = new AtomicLong(1);

    boolean temp = true;

    @GET
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "handleTimeout")
    //@RolesAllowed("PRODUCER")
    public Response search(
            @QueryParam("date") String date,
            @QueryParam("adId") Integer adId,
            @HeaderParam("Authorization") String auth
    ) {
        final Long invocationNumber = counter.getAndIncrement();
        long started = System.currentTimeMillis();
        try {
            boolean hasDelay = Boolean.parseBoolean(System.getProperty("ADBR_SEARCH_HAS_DELAY", "false"));

            LOGGER.infof("AdBroadcast search called");
            if (hasDelay) {
                LOGGER.infof("AdBroadcast search has delay invocation #%d ", invocationNumber);
                if (temp) {
                    temp = false;
                    Thread.sleep(20000L);
                }
            }
            List<AdBroadcast> found = adBroadcastService.search(date, adId, auth);
            return Response.ok().entity(adBroadcastMapper.toRepresentationList(found)).build();
        } catch (RadioException re) {
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode)
                    .entity(new ErrorResponse(re.getMessage()))
                    .build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Response handleTimeout(@QueryParam("date") String date,
                                  @QueryParam("adId") Integer adId,
                                  @HeaderParam("Authorization") String auth) {
        LOGGER.infof("Fallback - AdBroadcast The request timed out.");
        return Response.status(Response.Status.REQUEST_TIMEOUT)
                .entity("The request timed out. Please try again later.")
                .build();
    }

    @GET
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Path("/{id}")
    //@RolesAllowed("PRODUCER")
    public Response find(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            AdBroadcast adBroadcast = adBroadcastService.find(id, auth);
            return Response.ok().entity(adBroadcastMapper.toRepresentation(adBroadcast)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        } catch (RadioException re) {
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode)
                    .entity(new ErrorResponse(re.getMessage()))
                    .build();
        }
    }

    @Bulkhead(value = 20)
    @Timeout(value = 500, unit = ChronoUnit.SECONDS)
    @DELETE
    @Path("/{id}")
    //@RolesAllowed("PRODUCER")
    public Response delete(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            adBroadcastService.delete(id, auth);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        } catch (RadioException re) {
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode)
                    .entity(new ErrorResponse(re.getMessage()))
                    .build();
        }
    }


    @POST
    //@RolesAllowed("PRODUCER")
    public Response createAdBroadcast(
            AdBroadcastCreationDTO dto,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            AdBroadcast adBroadcast = adBroadcastService.create(dto, auth);
            URI uri = UriBuilder.fromResource(AdBroadcastResource.class).path(String.valueOf(adBroadcast.getId())).build();
            return Response.created(uri).entity(adBroadcastMapper.toRepresentation(adBroadcast)).build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        } catch (RadioException re) {
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode)
                    .entity(new ErrorResponse(re.getMessage()))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @Retry(maxRetries = 3)
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @DELETE
    public Response deleteByFilters(
            @HeaderParam("Authorization") String auth,
            @QueryParam("adId") Integer adId
    ) {
        try {
            final Long invocationNumber = counter.getAndIncrement();
            adBroadcastService.deleteByFilters(auth, adId);
            LOGGER.infof("AdBroadcast.deleteByFilters() invocation #%d returning successfully", invocationNumber);

            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        } catch (RadioException re) {
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode)
                    .entity(new ErrorResponse(re.getMessage()))
                    .build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        }
    }
}
