package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.application.GenreService;
import gr.aueb.radio.content.common.ErrorResponse;
import gr.aueb.radio.content.common.ExternalServiceException;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.infrastructure.rest.ApiPath;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreRepresentation;
import gr.aueb.radio.content.infrastructure.rest.representation.SongRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path(ApiPath.Root.GENRES)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class GenreResource {

    @Inject
    GenreService genreService;

//    @Inject
//    GenreMapper genreMapper;

    @GET
    @Path("/{id}")
    //@PermitAll
    public Response getGenreById(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            GenreRepresentation found = genreService.getGenreById(id, auth);
            return Response.ok().entity(found).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }  catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        } catch (RadioException re){
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode)
                    .entity(new ErrorResponse(re.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getAllGenres(
            @HeaderParam("Authorization") String auth) {
        try {
            List<GenreRepresentation> genres = genreService.getAllGenres(auth);
            return Response.ok().entity(genres).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }  catch (ExternalServiceException externalServiceException) {
            return Response.status(externalServiceException.getStatusCode())
                    .entity(new ErrorResponse(externalServiceException.getMessage()))
                    .build();
        } catch (RadioException re){
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode)
                    .entity(new ErrorResponse(re.getMessage()))
                    .build();
        }
    }

}
