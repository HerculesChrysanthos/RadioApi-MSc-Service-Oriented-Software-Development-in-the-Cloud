package gr.aueb.radio.resources;

import gr.aueb.radio.domains.AdBroadcast;
import gr.aueb.radio.dto.AdBroadcastCreationDTO;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.AdBroadcastMapper;
import gr.aueb.radio.services.AdBroadcastService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/ad_broadcasts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AdBroadcastResource {

    @Inject
    AdBroadcastService adBroadcastService;
    @Inject
    AdBroadcastMapper adBroadcastMapper;

    @Context
    UriInfo uriInfo;

    @GET
    @RolesAllowed("PRODUCER")
    public Response search(@QueryParam("date") String date) {
        try {
            List<AdBroadcast> found = adBroadcastService.search(date);
            return Response.ok().entity(adBroadcastMapper.toRepresentationList(found)).build();
        }catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("PRODUCER")
    public Response find(@PathParam("id") Integer id) {
        try {
            AdBroadcast adBroadcast = adBroadcastService.find(id);
            return Response.ok().entity(adBroadcastMapper.toRepresentation(adBroadcast)).build();
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("PRODUCER")
    public Response delete(@PathParam("id") Integer id) {
        try {
            adBroadcastService.delete(id);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        }catch (NotFoundException e){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        }
    }


    @POST
    @RolesAllowed("PRODUCER")
    public Response createAdBroadcast(AdBroadcastCreationDTO dto) {
        try {
            AdBroadcast adBroadcast = adBroadcastService.create(dto);
            URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(adBroadcast.getId())).build();
            return Response.created(uri).entity(adBroadcastMapper.toRepresentation(adBroadcast)).build();
        } catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
        }catch (NotFoundException re){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), re.getMessage()).build();
        }
    }
}
