package gr.aueb.radio.broadcast.infrastructure.rest.resource;

import gr.aueb.radio.broadcast.application.BroadcastService;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.enums.BroadcastEnum;
import gr.aueb.radio.broadcast.infrastructure.rest.ApiPath.Root;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastMapper;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastOutputRepresentation;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.OutputBroadcastMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path(Root.BROADCASTS)
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
    //@PermitAll
    @Transactional
    public Response getBroadcast(@PathParam("id") Integer id){
        try{
            Broadcast broadcast = broadcastService.findById(id);
            return Response.ok().entity(outputBroadcastMapper.toRepresentation(broadcast)).build();
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    @GET
//    //@PermitAll
//    public Response searchBroadcasts(@QueryParam("from") String from,
//                                     @QueryParam("to") String to,
//                                     @QueryParam("date") String date,
//                                     @QueryParam("type") BroadcastEnum type){
//        try {
//            BroadcastSearchDTO searchDTO = new BroadcastSearchDTO(from, to, date, type);
//            List<BroadcastOutputRepresentation> broadcastsFound = broadcastService.search(searchDTO);
//            return Response.ok().entity(broadcastsFound).build();
//        }catch (RadioException re){
//            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
//        }
//    }
//
//    @GET
//    @Path("now")
//    //@PermitAll
//    public Response getNow(){
//        BroadcastOutputRepresentation playingNow = broadcastService.getNow();
//        return Response.ok().entity(playingNow).build();
//    }
//
//    @POST
//    @RolesAllowed("PRODUCER")
//    public Response create(@Valid BroadcastRepresentation broadcastRepresentation){
//        try{
//            Broadcast broadcast = broadcastService.create(broadcastRepresentation);
//            URI uri = UriBuilder.fromResource(BroadcastResource.class).path(String.valueOf(broadcast.getId())).build();
//            return Response.created(uri).entity(outputBroadcastMapper.toRepresentation(broadcast)).build();
//        }catch (RadioException re){
//            log.error("Broadcast overlapping restriction triggered");
//            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
//        }
//    }
//
//
//    @PUT
//    @Path("/{id}")
//    //@RolesAllowed("PRODUCER")
//    public Response update(@PathParam("id") Integer id, @Valid BroadcastRepresentation broadcastRepresentation){
//        try{
//            broadcastService.update(id, broadcastRepresentation);
//            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
//        } catch (RadioException re){
//            log.error("Broadcast cannot be updated");
//            log.error(re.getMessage());
//            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
//        } catch (NotFoundException e){
//            log.error("Broadcast not found");
//            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
//        }
//    }
//
//    @Path("/{id}")
//    @DELETE
//    //@RolesAllowed("PRODUCER")
//    public Response delete(@PathParam("id") Integer id){
//        try{
//            broadcastService.delete(id);
//            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
//        } catch (NotFoundException e){
//            log.error("Broadcast not found");
//            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
//        }
//    }
//
//    @Path("/{id}/suggestions")
//    @GET
//    //@RolesAllowed("PRODUCER")
//    public Response suggest(@PathParam("id") Integer id){
//        try{
//            SuggestionsDTO dto = broadcastService.suggestions(id);
//            return Response.ok().entity(dto).build();
//        } catch (NotFoundException e){
//            log.error("Broadcast not found");
//            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
//        }
}