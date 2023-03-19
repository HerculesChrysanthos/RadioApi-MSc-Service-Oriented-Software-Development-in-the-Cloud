package gr.aueb.radio.resources;

import gr.aueb.radio.domains.Song;
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

@Path("/song")
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class SongResource {
    @Context
	UriInfo uriInfo;
    
    @Inject
    SongRepository songRepository;

    @Inject
    SongMapper songMapper;

    @GET
	@Transactional
	public List<SongRepresentation> listAll() {
		return songMapper.toRepresentationList(songRepository.listAll());
	}

	@GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response get(@PathParam("id") Integer id) {
        Song song = songRepository.findById(id);
        if (song == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok().entity(songMapper.toRepresentation(song)).build();
    }
	
    @GET
    @Path("/song?artist=")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<SongRepresentation> findSongsByArtist(@QueryParam("artist") String artist) {
		return songMapper.toRepresentationList(songRepository.findSongsByArtist(artist));
	}

    @GET
    @Path("/song?genre=")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<SongRepresentation> findSongsByGenre(@QueryParam("genre") String genre) {
		return songMapper.toRepresentationList(songRepository.findSongsByGenre(genre));
	}

    @GET
    @Path("/song?title=")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@QueryParam("title") String title) {
        Song a = songRepository.findSongByTitle(title);
        if (a == null) {
            return Response.status(Status.NOT_FOUND).build();
		}
		    return Response.ok().entity(songMapper.toRepresentation(a)).build();
	}
    
    @POST
    public Response create (SongRepresentation songRepresentation) {
        Song song = songMapper.toModel(songRepresentation);
        songRepository.persist(song);
        URI uri = UriBuilder.fromResource(SongResource.class).path(String.valueOf(song.getId())).build();
        SongRepresentation createdSongRepresentation = songMapper.toRepresentation(song);
        return Response.created(uri).entity(createdSongRepresentation).build();

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Integer id) {
        Song song = songRepository.findById(id);
        if (song == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        songRepository.delete(song);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Transactional   
    public Response update(@PathParam("id") Integer id, SongRepresentation songRepresentation) {
        Song song = songRepository.findById(id);
        if (song == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        songRepository.persist(song);
        return Response.noContent().build();
    }

}