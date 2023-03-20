package gr.aueb.radio.resources;

import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.dto.BroadcastSearchDTO;
import gr.aueb.radio.dto.SuggestionsDTO;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.BroadcastMapper;
import gr.aueb.radio.mappers.OutputBroadcastMapper;
import gr.aueb.radio.representations.BroadcastOutputRepresentation;
import gr.aueb.radio.representations.BroadcastRepresentation;
import gr.aueb.radio.services.BroadcastService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;


@Path("/broadcasts")
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
    OutputBroadcastMapper outputBroadcastMapper;

    @GET
    @Path("/{id}")
    @PermitAll
    public Response getBroadcast(@PathParam("id") Integer id){
        try{
            Broadcast broadcast = broadcastService.findById(id);
            return Response.ok().entity(broadcastMapper.toRepresentation(broadcast)).build();
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @PermitAll
    public Response searchBroadcasts(@QueryParam("from") String from,
                                     @QueryParam("to") String to,
                                     @QueryParam("date") String date,
                                     @QueryParam("type")BroadcastEnum type){
        try {
            BroadcastSearchDTO searchDTO = new BroadcastSearchDTO(from, to, date, type);
            List<BroadcastOutputRepresentation> broadcastsFound = broadcastService.search(searchDTO);
            return Response.ok().entity(broadcastsFound).build();
        }catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
        }
    }

    @POST
    @RolesAllowed("PRODUCER")
    public Response create(BroadcastRepresentation broadcastRepresentation){
        try{
            Broadcast broadcast = broadcastService.create(broadcastRepresentation);
            URI uri = UriBuilder.fromResource(BroadcastResource.class).path(String.valueOf(broadcast.getId())).build();
            return Response.created(uri).entity(outputBroadcastMapper.toRepresentation(broadcast)).build();
        }catch (RadioException re){
            log.error("Broadcast overlapping restriction triggered");
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
        }
    }


    @PUT
    @Path("/{id}")
    @RolesAllowed("PRODUCER")
    public Response update(@PathParam("id") Integer id, BroadcastRepresentation broadcastRepresentation){
        try{
            broadcastService.update(id, broadcastRepresentation);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        } catch (RadioException re){
            log.error("Broadcast cannot be updated");
            log.error(re.getMessage());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
        } catch (NotFoundException e){
            log.error("Broadcast not found");
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @RolesAllowed("PRODUCER")
    public Response delete(@PathParam("id") Integer id){
        try{
            broadcastService.delete(id);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        } catch (NotFoundException e){
            log.error("Broadcast not found");
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
    }

    @Path("/{id}/suggestions")
    @GET
    @RolesAllowed("PRODUCER")
    public Response suggest(@PathParam("id") Integer id){
        try{
            SuggestionsDTO dto = broadcastService.suggestions(id);
            return Response.ok().entity(dto).build();
        } catch (NotFoundException e){
            log.error("Broadcast not found");
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
    }
}
