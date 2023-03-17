package gr.aueb.radio.resources;

import gr.aueb.radio.domains.Add;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.AdMapper;
import gr.aueb.radio.representations.AdRepresentation;
import gr.aueb.radio.services.AdService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("/adds")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AdResource {
        @Inject
        AdService adService;
        @Inject
        AdMapper adMapper;

        @Context
        UriInfo uriInfo;

        @GET
        @Path("/{id}")
        public Response getAd(@PathParam("id") Integer id) {
                try {
                        AdRepresentation adRepresentation = adService.findAdd(id);
                        return Response.ok().entity(adRepresentation).build();
                } catch (NotFoundException nfe) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                }
        }

        @GET
        public Response getAdsOfTimezone(@QueryParam("timezone") ZoneEnum timezone) {
                List<AdRepresentation> adsByTimezone = adService.search(timezone);
                return Response.ok().entity(adsByTimezone).build();
        }

        @POST
        public Response createAdd(@PathParam("id") Integer id, AdRepresentation adrepresentation) {
                try {
                        Add add = adService.create(adrepresentation);
                        URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(add.getId())).build();
                        return Response.created(uri).entity(adMapper.toRepresentation(add)).build();
                }catch (RadioException re){
                        return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
                }
        }

}


