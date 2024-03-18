package gr.aueb.radio.content.infrastructure.service.user;

import gr.aueb.radio.content.application.UserService;
import gr.aueb.radio.content.common.ExternalServiceException;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import java.util.Base64;

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
        } catch(ProcessingException error) {
            throw new ExternalServiceException("Problem on reaching user api.");
        } catch(ResteasyWebApplicationException resteasyWebApplicationException) {
            throw new RadioException("User api got timeout", 408);
        } catch (CircuitBreakerOpenException circuitBreakerOpenException) {
            throw new RadioException("User api keeps getting errors", 422);

        }

    }
}
