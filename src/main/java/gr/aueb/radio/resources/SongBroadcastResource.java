package gr.aueb.radio.resources;

import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.dto.SongBroadcastCreationDTO;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.SongBroadcastMapper;
import gr.aueb.radio.services.SongBroadcastService;

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
    public Response search(@QueryParam("date") String date) {
        List<SongBroadcast> found = songBroadcastService.search(date);
        return Response.ok().entity(songBroadcastMapper.toRepresentationList(found)).build();
    }

    @GET
    @Path("/{id}")
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
    public Response delete(@PathParam("id") Integer id) {
        try {
            songBroadcastService.delete(id);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        }
    }


    @POST
    public Response createAdBroadcast(SongBroadcastCreationDTO dto) {
        try {
            SongBroadcast songBroadcast = songBroadcastService.create(dto);
            URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(songBroadcast.getId())).build();
            return Response.created(uri).entity(songBroadcastMapper.toRepresentation(songBroadcast)).build();
        } catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
        }catch (NotFoundException re){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), re.getMessage()).build();
        }
    }
}
