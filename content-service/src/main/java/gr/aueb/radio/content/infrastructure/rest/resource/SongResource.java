package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.application.SongService;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.rest.ApiPath.Root;
import gr.aueb.radio.content.infrastructure.rest.representation.SongInputDTO;
import gr.aueb.radio.content.infrastructure.rest.representation.SongMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.SongRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path(Root.SONGS)
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
    //@PermitAll
    public Response getSong(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            SongRepresentation found = songService.findSong(id, auth);
            return Response.ok().entity(found).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    //@PermitAll
    public Response search(
            @QueryParam("artist") String artist,
            @QueryParam("genre") String genre,
            @QueryParam("title") String title,
            @QueryParam("songsIds") String songsIds,
            @HeaderParam("Authorization") String auth
    ) {
        List<Integer> convertedSongsId = Arrays.stream(songsIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<SongRepresentation> found = songService.search(artist, genre, title, convertedSongsId, auth);
        return Response.ok().entity(found).build();
    }

    @POST
    //@PermitAll
    public Response create(
            @Valid SongInputDTO songRepresentation,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            Song song = songService.create(songRepresentation.toRepresentation(), auth);
            URI uri = UriBuilder.fromResource(SongResource.class).path(String.valueOf(song.getId())).build();
            SongRepresentation createdSongRepresentation = songMapper.toRepresentation(song);
            return Response.created(uri).entity(createdSongRepresentation).build();
        } catch (
                RadioException re) {
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode).entity(re.getMessage()).build();
        }

    }

    @DELETE
    @Path("/{id}")
    //@RolesAllowed("PRODUCER")
    public Response delete(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth) {
        try{
            songService.delete(id, auth);
            return Response.noContent().build();
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    //@PermitAll
    public Response update(
            @PathParam("id") Integer id,
            @Valid SongInputDTO songRepresentation,
            @HeaderParam("Authorization") String auth
    ) {
        try{
            songService.update(id, songRepresentation.toRepresentation(), auth);
            return Response.noContent().build();
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }catch (RadioException e){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(e.getMessage()).build();
        }
    }
}
