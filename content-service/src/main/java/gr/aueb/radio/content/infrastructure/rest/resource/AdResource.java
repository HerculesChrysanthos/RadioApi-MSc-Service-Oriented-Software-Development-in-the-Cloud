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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public Response getAd(@PathParam("id") Integer id,
                          @HeaderParam("Authorization") String auth) {
        try {
            AdRepresentation adRepresentation = adService.findAd(id, auth);
            return Response.ok().entity(adRepresentation).build();
        } catch (NotFoundException nfe) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
//    @RolesAllowed("PRODUCER")
    public Response search(
            @QueryParam("timezone") Zone timezone,
            @QueryParam("adsIds") String adsIds,
            @HeaderParam("Authorization") String auth
    ) {
        List<Integer> convertedAdsId = new ArrayList<>();
        if(adsIds != null) {
            convertedAdsId = Arrays.stream(adsIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }

        List<AdRepresentation> adsFound = adService.search(timezone, convertedAdsId, auth);
        return Response.ok().entity(adsFound).build();
    }

    @POST
//    @RolesAllowed("PRODUCER")
    public Response createAd(
            @Valid AdInputDTO adRepresentation,
            @HeaderParam("Authorization") String auth
    ) {
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
    public Response updateAd(
            @PathParam("id") Integer id,
            @Valid AdInputDTO adRepresentation,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            adService.update(id, adRepresentation.toRepresentation(), auth);
            return  Response.noContent().build();
        } catch (NotFoundException re){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        } catch (RadioException re){
            int statusCode = re.getStatusCode() != 0 ? re.getStatusCode() : Response.Status.BAD_REQUEST.getStatusCode();
            return Response.status(statusCode).entity(re.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
//    @RolesAllowed("PRODUCER")
    public Response deleteAd(
            @PathParam("id") Integer id,
            @HeaderParam("Authorization") String auth
    ) {
        try {
            adService.delete(id, auth);
            return Response.status(Response.Status.NO_CONTENT.getStatusCode()).build();
        }catch (NotFoundException re){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        }
    }
}
