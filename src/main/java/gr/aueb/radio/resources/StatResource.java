package gr.aueb.radio.resources;

import gr.aueb.radio.dto.AdStatsDTO;
import gr.aueb.radio.representations.BroadcastOutputRepresentation;
import gr.aueb.radio.representations.BroadcastRepresentation;
import gr.aueb.radio.services.StatService;
import gr.aueb.radio.utils.DateUtil;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class StatResource {

    @Inject
    StatService statService;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/program")
    @RolesAllowed("PRODUCER")
    public Response getDailySchedule(@QueryParam("date") String date){
            List<BroadcastOutputRepresentation> broadcastsOfDay = statService.getDailySchedule(date);
            return Response.ok().entity(broadcastsOfDay).build();
    }

    @GET
    @Path("/ads")
    @RolesAllowed("PRODUCER")
    public Response getAdsStats(@QueryParam("date") String date){
        AdStatsDTO dto = statService.extractAdStats(date);
        return Response.ok().entity(dto).build();
    }



}



