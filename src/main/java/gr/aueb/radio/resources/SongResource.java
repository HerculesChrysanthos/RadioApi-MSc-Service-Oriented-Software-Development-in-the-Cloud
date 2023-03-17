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
    @Path("/{artist}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<SongRepresentation> findSongsByArtist(@QueryParam("artist") String artist) {
		return songMapper.toRepresentationList(songRepository.findSongsByArtist(artist));
	}

    @GET
    @Path("/{genre}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<SongRepresentation> findSongsByGenre(@QueryParam("genre") String genre) {
		return songMapper.toRepresentationList(songRepository.findSongsByGenre(genre));
	}

    @GET
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SongRepresentation find(@PathParam("title") String title) {
        Song a = songRepository.findSongByTitle(title);
        if (a == null) {
            throw new NotFoundException();
		}
		return songMapper.toRepresentation(a);
	}
    
    @PUT
	@Transactional
	public Response create (SongRepresentation representation) {
		if (representation.title == null) {
			throw new RuntimeException();			
		}
		Song song = songMapper.toModel(representation);
		songRepository.persist(song);
		URI uri = UriBuilder.fromResource(SongResource.class).path(String.valueOf(song.getTitle())).build();
		return Response.created(uri).entity(songMapper.toRepresentation(song)).build();

    }

}