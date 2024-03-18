package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.common.ErrorResponse;
import gr.aueb.radio.content.common.ExternalServiceException;
import gr.aueb.radio.content.infrastructure.rest.ApiPath;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import gr.aueb.radio.content.application.AdService;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.infrastructure.rest.ApiPath.Root;
import gr.aueb.radio.content.infrastructure.rest.representation.AdInputDTO;
import gr.aueb.radio.content.infrastructure.rest.representation.AdMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.faulttolerance.Bulkhead;

import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Path(ApiPath.Root.ADS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AdResource {

    private static final Logger LOGGER = Logger.getLogger(AdResource.class);

    @Context
    UriInfo uriInfo;

    @Inject
    AdMapper adMapper;

    @Inject
    AdService adService;
    boolean temp = true;

    @Timeout(10000)
    @GET
    @Path("/{id}")
    public Response getAd(@PathParam("id") Integer id,
                          @HeaderParam("Authorization") String auth) throws InterruptedException {
        try {
            System.out.println("mpika - " + temp);

            // trigger content service unavailability - rest for retries
            boolean hasDelay = Boolean.parseBoolean(System.getProperty("CONTENT_HAS_DELAY", "false"));
            if (hasDelay) {
                LOGGER.infof("findAd().hadDelay set true");
                if (temp) {
                    temp = false;
                    System.out.println("temp - " + temp);
                    Thread.sleep(12000);
                }
            }
            System.out.println("temp after - " + temp);
            AdRepresentation adRepresentation = adService.findAd(id, auth);
            LOGGER.infof("findAd() returning successfully - id " + id);
            return Response.ok().entity(adRepresentation).build();
        } catch (NotFoundException nfe) {
            return Response.status(Response.Status.NOT_FOUND).build();
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

    @GET
    @Fallback(fallbackMethod = "fallbackAdRecommendations")
    @Timeout(5000)
    public Response search(
            @QueryParam("timezone") Zone timezone,
            @QueryParam("adsIds") String adsIds,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            List<Integer> convertedAdsId = new ArrayList<>();
            if (adsIds != null) {
                convertedAdsId = Arrays.stream(adsIds.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
            }
            boolean hasDelay = Boolean.parseBoolean(System.getProperty("CONTENT_HAS_DELAY", "false"));
            if (hasDelay) {
                LOGGER.infof("Content ads search has delay");
                try {
                    LOGGER.infof("search()  thread sleep 10000");
                    // Simulate timeout by sleeping for longer than the timeout duration
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // Handle interruption
                    Thread.currentThread().interrupt();
                }
            }

            LOGGER.infof("search()  returning successfully");
            List<AdRepresentation> adsFound = adService.search(timezone, convertedAdsId, auth);
            return Response.ok().entity(adsFound).build();
        } catch (RadioException re) {
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            LOGGER.infof("AdResource.search() statusCode");
            return Response.status(statusCode)
                    .entity(new ErrorResponse(re.getMessage()))
                    .build();
        } catch (ExternalServiceException externalServiceException) {
            LOGGER.infof("AdResource.search() statusCode " + externalServiceException.getStatusCode());
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        }
    }

    public Response fallbackAdRecommendations(
            @QueryParam("timezone") Zone timezone,
            @QueryParam("adsIds") String adsIds,
            @HeaderParam("Authorization") String auth
    ) {
        LOGGER.info("Falling back to fallbackAdRecommendations()");
        if (timezone != null || adsIds != null) {
            List<AdRepresentation> fallbackAds = adService.searchAdFallback(auth);
            LOGGER.info("fallbackAdRecommendations().fallbackAds " + fallbackAds.size());

            return Response.ok().entity(fallbackAds).build();
        }
        return null;
    }

    @POST
//    @RolesAllowed("PRODUCER")
    public Response createAd(
            @Valid AdInputDTO adRepresentation,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            Ad ad = adService.create(adRepresentation.toRepresentation(), auth);
            URI uri = UriBuilder.fromResource(AdResource.class).path(String.valueOf(ad.getId())).build();
            return Response.created(uri).entity(adMapper.toRepresentation(ad)).build();
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

    @PUT
    @Path("/{id}")
//    @RolesAllowed("PRODUCER")
    public Response updateAd(
            @PathParam("id") Integer id,
            @Valid AdInputDTO adRepresentation,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            adService.update(id, adRepresentation.toRepresentation(), auth);
            return Response.noContent().build();
        } catch (NotFoundException re) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
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

    @DELETE
    @Path("/{id}")
//    @RolesAllowed("PRODUCER")
    public Response deleteAd(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            adService.delete(id, auth);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        } catch (NotFoundException re) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
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
