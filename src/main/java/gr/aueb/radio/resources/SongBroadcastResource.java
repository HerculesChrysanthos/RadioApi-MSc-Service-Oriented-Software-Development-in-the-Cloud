package gr.aueb.radio.resources;

import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.dto.SongBroadcastCreationDTO;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.SongBroadcastMapper;
import gr.aueb.radio.services.SongBroadcastService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/song_broadcasts")
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
    @RolesAllowed("PRODUCER")
    public Response search(@QueryParam("date") String date) {
        try {
            List<SongBroadcast> found = songBroadcastService.search(date);
            return Response.ok().entity(songBroadcastMapper.toRepresentationList(found)).build();
        }catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("PRODUCER")
    public Response find(@PathParam("id") Integer id) {
        try {
            SongBroadcast songBroadcast = songBroadcastService.find(id);
            return Response.ok().entity(songBroadcastMapper.toRepresentation(songBroadcast)).build();
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("PRODUCER")
    public Response delete(@PathParam("id") Integer id) {
        try {
            songBroadcastService.delete(id);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        }
    }


    @POST
    @RolesAllowed("PRODUCER")
    public Response createSongBroadcast(SongBroadcastCreationDTO dto) {
        try {
            SongBroadcast songBroadcast = songBroadcastService.create(dto);
            URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(songBroadcast.getId())).build();
            return Response.created(uri).entity(songBroadcastMapper.toRepresentation(songBroadcast)).build();
        } catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
        } catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
    }
}
