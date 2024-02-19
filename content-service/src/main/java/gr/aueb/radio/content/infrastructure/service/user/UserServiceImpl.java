package gr.aueb.radio.content.infrastructure.service.user;

import gr.aueb.radio.content.application.UserService;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

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
        } catch(ProcessingException error){
            throw new RadioException("Problem on reaching user api.", 424);
        }

    }
}
