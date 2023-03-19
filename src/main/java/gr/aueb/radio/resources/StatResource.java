package gr.aueb.radio.resources;

import gr.aueb.radio.representations.BroadcastRepresentation;
import gr.aueb.radio.services.StatService;
import gr.aueb.radio.utils.DateUtil;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
public class StatResource {

    @Inject
    StatService statService;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{date}")
    public Response getDailySchedule(@PathParam("date") String date){
            List<BroadcastRepresentation> broadcastsOfDay = statService.getDailySchedule(DateUtil.setDate(date));
            return Response.ok().entity(broadcastsOfDay).build();
    }



}



