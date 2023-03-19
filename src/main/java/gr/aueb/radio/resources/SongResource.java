package gr.aueb.radio.resources;

import gr.aueb.radio.domains.Song;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.SongMapper;
import gr.aueb.radio.persistence.SongRepository;
import gr.aueb.radio.representations.SongRepresentation;
import gr.aueb.radio.services.SongService;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/songs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class SongResource {
    @Context
	UriInfo uriInfo;

    @Inject
    SongMapper songMapper;

    @Inject
    SongService songService;


	@GET
    @Path("/{id}")
    public Response getSong(@PathParam("id") Integer id) {
        try{
            SongRepresentation found = songService.findSong(id);
            return Response.ok().entity(found).build();
        }catch (NotFoundException e){
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @GET
    public Response search(@QueryParam("artist") String artist,
                         @QueryParam("genre") String genre,
                         @QueryParam("title") String title) {
        List<SongRepresentation> found = songService.search(artist, genre, title);
        return Response.ok().entity(found).build();
    }
    
    @POST
    public Response create (SongRepresentation songRepresentation) {
        Song song = songService.create(songRepresentation);
        URI uri = UriBuilder.fromResource(SongResource.class).path(String.valueOf(song.getId())).build();
        SongRepresentation createdSongRepresentation = songMapper.toRepresentation(song);
        return Response.created(uri).entity(createdSongRepresentation).build();

    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        try{
            songService.delete(id);
            return Response.noContent().build();
        }catch (NotFoundException e){
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, SongRepresentation songRepresentation) {
        try{
            songService.update(id, songRepresentation);
            return Response.noContent().build();
        }catch (NotFoundException e){
            return Response.status(Status.NOT_FOUND).build();
        }catch (RadioException e){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage()).build();
        }
    }

}