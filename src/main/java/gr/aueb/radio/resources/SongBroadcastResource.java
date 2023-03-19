package gr.aueb.radio.resources;

import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.SongBroadcastMapper;
import gr.aueb.radio.persistence.SongBroadcastRepository;
import gr.aueb.radio.representations.SongBroadcastRepresentation;
import gr.aueb.radio.services.SongBroadcastService;

import java.net.URI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Path("/songBroadcast")
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

    @POST
    public Response createSongBroadcast(SongBroadcastRepresentation songBroadcastRepresentation) {
        try {
            SongBroadcast songBroadcast = songBroadcastService.create(songBroadcastRepresentation);
            URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(songBroadcast.getId())).build();
            return Response.created(uri).entity(songBroadcastMapper.toRepresentation(songBroadcast)).build();
        }
        catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
        }
    }
}
