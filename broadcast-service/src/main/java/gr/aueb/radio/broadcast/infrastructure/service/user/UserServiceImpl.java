package gr.aueb.radio.broadcast.infrastructure.service.user;

import gr.aueb.radio.broadcast.application.UserService;
import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    Logger logger;

    @Inject
    @RestClient
    UserApi userApi;

    @Override
    public UserVerifiedRepresentation verifyAuth(String auth) {
        try {
            UserVerifiedRepresentation user = userApi.verifyAuth(auth);
            System.out.println("returned id "+ user.id);

            return user;
        } catch (ProcessingException error) {
            throw new ExternalServiceException("Problem on reaching user api.");
        } catch (WebApplicationException webApplicationException){
            throw new RadioException("Auth error", webApplicationException.getResponse().getStatus());
        } catch (CircuitBreakerOpenException circuitBreakerOpenException) {
            throw new RadioException("User api keeps getting errors", 422);

        }

    }
}
