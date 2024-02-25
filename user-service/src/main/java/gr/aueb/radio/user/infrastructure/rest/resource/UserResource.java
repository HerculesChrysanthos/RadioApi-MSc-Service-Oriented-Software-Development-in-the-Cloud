package gr.aueb.radio.user.infrastructure.rest.resource;

import gr.aueb.radio.user.application.UserService;
import gr.aueb.radio.user.common.RadioException;
import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.rest.ApiPath.Root;
import gr.aueb.radio.user.infrastructure.rest.representation.UserBasicRepresentation;
import gr.aueb.radio.user.infrastructure.rest.representation.UserInputDTO;
import gr.aueb.radio.user.infrastructure.rest.representation.UserMapper;
import gr.aueb.radio.user.infrastructure.rest.representation.UserRepresentation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;

@RequestScoped
@Path(Root.USERS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    public
    UserService userService;

    @Inject
    public
    UserMapper userMapper;

    @Inject
    public
    SecurityContext securityContext;

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Integer id){
        try {
            UserRepresentation userRepresentation = userService.findUser(id);
            return Response.ok().entity(userRepresentation).build();
        }catch (NotFoundException nfe){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    @POST
    public Response register(@Valid UserInputDTO userRepresentation){
        try {
            User user = userService.create(userRepresentation.toRepresentation());
            URI uri = UriBuilder.fromResource(UserResource.class).path(String.valueOf(user.getId())).build();
            return Response.created(uri).entity(userMapper.toRepresentation(user)).build();
        }catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
        }
    }

    @GET
    @RolesAllowed({"USER", "PRODUCER"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/verify-auth")
    public Response verifyAuth(){
        try {
            String username = securityContext.getUserPrincipal().getName();
            UserBasicRepresentation user = userService.findUserByUsername(username);
            return Response.ok().entity(user).build();
        } catch (RadioException re){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(re.getMessage()).build();
        }
    }
}
