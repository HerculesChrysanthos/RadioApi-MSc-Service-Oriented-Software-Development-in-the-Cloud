package gr.aueb.radio.broadcast.infrastructure.rest.resource;

import gr.aueb.radio.broadcast.application.BroadcastService;
import gr.aueb.radio.broadcast.application.StatService;
import gr.aueb.radio.broadcast.common.ErrorResponse;
import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import gr.aueb.radio.broadcast.infrastructure.rest.ApiPath.Root;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.*;
import io.quarkus.logging.Log;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Path(Root.BROADCASTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@Slf4j
public class BroadcastResource {
    @Inject
    BroadcastService broadcastService;

    @Inject
    BroadcastMapper broadcastMapper;

    @Inject
    StatService statService;

    @Inject
    OutputBroadcastMapper outputBroadcastMapper;

    @GET
    @Timeout(10000)
    @Retry(maxRetries = 3, delay = 2, delayUnit = ChronoUnit.SECONDS)
    @Path("/{id}")
    //@PermitAll
    @Transactional
    public Response getBroadcast(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try{
            Broadcast broadcast = broadcastService.findById(id, auth);
            return Response.ok()
                    .entity(outputBroadcastMapper.toRepresentation(broadcast))
                    .build();
        } catch (ExternalServiceException externalServiceException){
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        } catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    //@PermitAll
    public Response searchBroadcasts(@QueryParam("from") String from,
                                     @QueryParam("to") String to,
                                     @QueryParam("date") String date,
                                     @QueryParam("type") String typeParam){

        BroadcastType type = null;
        if (typeParam != null && !typeParam.isEmpty()) {
            try {
                type = BroadcastType.valueOf(typeParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                String[] acceptedValues = Arrays.stream(BroadcastType.values())
                        .map(Enum::name)
                        .toArray(String[]::new);

                return Response.status(422)
                        .entity(new ErrorResponse("Invalid value for 'type' parameter.", acceptedValues))
                        .build();
            }
        }

        BroadcastSearchDTO searchDTO = new BroadcastSearchDTO(from, to, date, type);
        List<BroadcastOutputRepresentation> broadcastsFound = broadcastService.search(searchDTO);
        return Response.ok().entity(broadcastsFound).build();

    }

    @GET
    @Path("now")
    //@PermitAll
    public Response getNow(
            @HeaderParam("Authorization") String auth
    ) {
        try {
            BroadcastOutputRepresentation playingNow = broadcastService.getNow(auth);
            return Response.ok().entity(playingNow).build();
        } catch (ExternalServiceException externalServiceException){
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
//    @RolesAllowed("PRODUCER")
    public Response create(
            @Valid BroadcastRepresentation broadcastRepresentation,
            @HeaderParam("Authorization") String auth
    ) {
        try{
            Broadcast broadcast = broadcastService.create(broadcastRepresentation, auth);
            URI uri = UriBuilder.fromResource(BroadcastResource.class).path(String.valueOf(broadcast.getId())).build();
            return Response.created(uri).entity(outputBroadcastMapper.toRepresentation(broadcast)).build();
        } catch (RadioException re){
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
//
//
    @PUT
    @Path("/{id}")
    //@RolesAllowed("PRODUCER")
    public Response update(
            @PathParam("id") Integer id,
            @Valid BroadcastRepresentation broadcastRepresentation,
            @HeaderParam("Authorization") String auth
    ) {
        try{
            broadcastService.update(id, broadcastRepresentation, auth);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        } catch (RadioException re){
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode)
                    .entity(new ErrorResponse(re.getMessage()))
                    .build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        } catch (NotFoundException e){
            //log.error("Broadcast not found");
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    //@RolesAllowed("PRODUCER")
    public Response delete(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try{
            broadcastService.delete(id, auth);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        } catch (NotFoundException e){
            Log.error("Broadcast not found");
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch (RadioException re){
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

    @Path("/{id}/suggestions")
    @Timeout(10000)
    @Retry(maxRetries = 3, delay = 2, delayUnit = ChronoUnit.SECONDS)
    @GET
    //@RolesAllowed("PRODUCER")
    public Response suggest(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            SuggestionsDTO dto = broadcastService.suggestions(id, auth);
            return Response.ok().entity(dto).build();
        } catch (NotFoundException e) {
            Log.error("Broadcast not found");
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch (RadioException re){
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
    @Timeout(10000)
    @Retry(maxRetries = 3, delay = 2, delayUnit = ChronoUnit.SECONDS)
    @Path("/stats-daily")
    public Response getDailySchedule(
            @QueryParam("date") String date,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            List<BroadcastOutputRepresentation> broadcastsOfDay = statService.getDailySchedule(date, auth);
            return Response.ok().entity(broadcastsOfDay).build();
        } catch (RadioException re){
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
    @Path("/stats-ads")
    public Response getAdsStats(
            @QueryParam("date") String date,
            @HeaderParam("Authorization") String auth)
    {
        try {
            AdStatsDTO dto = statService.extractAdStats(date, auth);
            return Response.ok().entity(dto).build();
        } catch (RadioException re){
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