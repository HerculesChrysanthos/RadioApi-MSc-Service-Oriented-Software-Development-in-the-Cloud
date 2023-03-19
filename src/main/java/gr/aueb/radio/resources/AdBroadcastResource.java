package gr.aueb.radio.resources;

import gr.aueb.radio.domains.AdBroadcast;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.AdBroadcastMapper;
import gr.aueb.radio.representations.AdBroadcastRepresentation;
import gr.aueb.radio.services.AdBroadcastService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.net.URI;

@Path("/adBroadcast")
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

    @POST
    public Response createAdBroadcast(AdBroadcastRepresentation adBroadcastRepresentation) {
        try {
            AdBroadcast adBroadcast = adBroadcastService.create(adBroadcastRepresentation);
            URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(adBroadcast.getId())).build();
            return Response.created(uri).entity(adBroadcastMapper.toRepresentation(adBroadcast)).build();
        }
        catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
        }
    }
}
