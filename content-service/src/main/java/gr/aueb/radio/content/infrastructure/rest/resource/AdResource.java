package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.infrastructure.rest.ApiPath;
import jakarta.inject.Inject;
import gr.aueb.radio.content.application.AdService;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.infrastructure.rest.ApiPath.Root;
import gr.aueb.radio.content.infrastructure.rest.representation.AdInputDTO;
import gr.aueb.radio.content.infrastructure.rest.representation.AdMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
@Path(ApiPath.Root.ADS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class AdResource {

    @Context
    UriInfo uriInfo;

    @Inject
    AdMapper adMapper;

    @Inject
    AdService adService;
    @GET
    @Path("/{id}")
//    @RolesAllowed("PRODUCER")
    public Response getAd(@PathParam("id") Integer id) {
        try {
            AdRepresentation adRepresentation = adService.findAd(id);
            return Response.ok().entity(adRepresentation).build();
        } catch (NotFoundException nfe) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
//    @RolesAllowed("PRODUCER")
    public Response getAdsOfTimezone(@QueryParam("timezone") Zone timezone) {
        List<AdRepresentation> adsByTimezone = adService.search(timezone);
        return Response.ok().entity(adsByTimezone).build();
    }

    @POST
//    @RolesAllowed("PRODUCER")
    public Response createAd(@Valid AdInputDTO adRepresentation, @HeaderParam("Authorization") String auth) {
        try {
            Ad ad = adService.create(adRepresentation.toRepresentation(), auth);
            URI uri = UriBuilder.fromResource(AdResource.class).path(String.valueOf(ad.getId())).build();
            return Response.created(uri).entity(adMapper.toRepresentation(ad)).build();
        } catch (RadioException re){
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode).entity(re.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
//    @RolesAllowed("PRODUCER")
    public Response updateAd(@PathParam("id") Integer id, @Valid AdInputDTO adrepresentation) {
        try {
            adService.update(id, adrepresentation.toRepresentation());
            return  Response.noContent().build();
        } catch (NotFoundException re){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
//    @RolesAllowed("PRODUCER")
    public Response deleteAd(@PathParam("id") Integer id) {
        try {
            adService.delete(id);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        }catch (NotFoundException re){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
    }
}
