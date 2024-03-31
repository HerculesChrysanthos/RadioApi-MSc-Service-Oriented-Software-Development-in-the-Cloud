package gr.aueb.radio.user.infrastructure.rest.resource;

import gr.aueb.radio.user.application.UserService;
import gr.aueb.radio.user.common.RadioException;
import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.rest.ApiPath.Root;
import gr.aueb.radio.user.infrastructure.rest.representation.UserBasicRepresentation;
import gr.aueb.radio.user.infrastructure.rest.representation.UserInputDTO;
import gr.aueb.radio.user.infrastructure.rest.representation.UserMapper;
import gr.aueb.radio.user.infrastructure.rest.representation.UserRepresentation;
import io.quarkus.logging.Log;
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
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;

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

    private static final Logger LOGGER = Logger.getLogger(UserResource.class);
    private AtomicLong counter = new AtomicLong(0);

    @Timeout(20000)
    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Integer id){
        final Long invocationNumber = counter.getAndIncrement();
        try {
            UserRepresentation userRepresentation = userService.findUser(id);
            return Response.ok().entity(userRepresentation).build();
        }catch (NotFoundException nfe){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }



    @Timeout(7000)
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

    /*
    @Timeout(10000)
    @RolesAllowed({"USER", "PRODUCER"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Counted(name = "performedChecks", description = "How many primality checks have been performed.")
    @Timed(name = "checksTimer", description = "A measure of how long it takes to perform the primality test.", unit = MetricUnits.MILLISECONDS)

     */
    @GET
    @Timeout(5000)
    @Path("/verify-auth")
    public Response verifyAuth(){
        final Long invocationNumber = counter.getAndIncrement();
        String username = securityContext.getUserPrincipal().getName();
        UserBasicRepresentation user = userService.findUserByUsername(username);

        boolean hasDelay = Boolean.parseBoolean(System.getProperty("USER_HAS_DELAY", "false"));

        LOGGER.infof("User verify auth called, invocation #%d ", invocationNumber);
        if(hasDelay) {
            LOGGER.infof("User verify auth has delay invocation #%d ", invocationNumber);
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {

            }
        }

        return Response.ok().entity(user).build();

    }
}
