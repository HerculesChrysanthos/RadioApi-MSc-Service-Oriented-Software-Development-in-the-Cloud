package gr.aueb.radio.broadcast.infrastructure.rest.resource;

import gr.aueb.radio.broadcast.common.ErrorResponse;
import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.application.SongBroadcastService;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.rest.ApiPath.Root;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.SongBroadcastCreationDTO;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.SongBroadcastMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;

@Path(Root.SONG_BROADCASTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class SongBroadcastResource {

    @Inject
    SongBroadcastService songBroadcastService;
    @Inject
    SongBroadcastMapper songBroadcastMapper;

    @Context
    UriInfo uriInfo;

    @GET
    //@RolesAllowed("PRODUCER")
    public Response search(
            @QueryParam("date") String date,
            @QueryParam("songId") Integer songId,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            List<SongBroadcast> found = songBroadcastService.search(date, songId, auth);
            return Response.ok().entity(songBroadcastMapper.toRepresentationList(found)).build();
        } catch (RadioException re) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    //@RolesAllowed("PRODUCER")
    public Response find(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            SongBroadcast songBroadcast = songBroadcastService.find(id, auth);
            return Response.ok().entity(songBroadcastMapper.toRepresentation(songBroadcast)).build();
        } catch (RadioException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    //@RolesAllowed("PRODUCER")
    public Response delete(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            songBroadcastService.delete(id, auth);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        }

    }


    @POST
    //@RolesAllowed("PRODUCER")
    public Response createSongBroadcast(
            SongBroadcastCreationDTO dto,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            SongBroadcast songBroadcast = songBroadcastService.create(dto, auth);
            URI uri = UriBuilder.fromResource(SongBroadcastResource.class).path(String.valueOf(songBroadcast.getId())).build();
            return Response.created(uri).entity(songBroadcastMapper.toRepresentation(songBroadcast)).build();
        } catch (RadioException re) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        }
    }

    @DELETE
    public Response deleteByFilters(
            @HeaderParam("Authorization") String auth,
            @QueryParam("songId") Integer songId
    ) {
        try {
            songBroadcastService.deleteByFilters(auth, songId);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        } catch (RadioException re) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
        } catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        }
    }
}