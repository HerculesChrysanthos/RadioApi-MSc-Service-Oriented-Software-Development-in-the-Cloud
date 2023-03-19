package gr.aueb.radio.resources;

import gr.aueb.radio.domains.Ad;
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

@Path("/ads")
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
                        AdRepresentation adRepresentation = adService.findAd(id);
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
        public Response createAd(AdRepresentation adrepresentation) {
                try {
                        Ad ad = adService.create(adrepresentation);
                        URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(ad.getId())).build();
                        return Response.created(uri).entity(adMapper.toRepresentation(ad)).build();
                }catch (RadioException re){
                        return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
                }
        }

        @PUT
        @Path("/{id}")
        public Response updateAd(@PathParam("id") Integer id, AdRepresentation adrepresentation) {
                try {
                        adService.update(id, adrepresentation);
                        return  Response.noContent().build();
                }
                catch (NotFoundException re){
                        return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
                }
        }

        @DELETE
        @Path("/{id}")
        public Response deleteAd(@PathParam("id") Integer id, AdRepresentation adrepresentation) {
                try {
                        adService.delete(id,adrepresentation);
                        return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
                }catch (NotFoundException re){
                        return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
                }
        }
}


