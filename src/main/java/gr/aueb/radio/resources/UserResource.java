package gr.aueb.radio.resources;

import gr.aueb.radio.domains.User;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.UserMapper;
import gr.aueb.radio.representations.UserRepresentation;
import gr.aueb.radio.services.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserResource {
    @Inject
    UserService userService;

    @Inject
    UserMapper userMapper;

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Integer id){
        try {
            UserRepresentation userRepresentation = userService.findUser(id);
            return Response.ok().entity(userRepresentation).build();
        }catch (NotFoundException nfe){
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    @POST
    public Response register(UserRepresentation userRepresentation){
        try {
            User user = userService.create(userRepresentation);
            URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(user.getId())).build();
            return Response.created(uri).entity(userMapper.toRepresentation(user)).build();
        }catch (RadioException re){
            return Response.status(Status.BAD_REQUEST.getStatusCode(), re.getMessage()).build();
        }
    }
}
