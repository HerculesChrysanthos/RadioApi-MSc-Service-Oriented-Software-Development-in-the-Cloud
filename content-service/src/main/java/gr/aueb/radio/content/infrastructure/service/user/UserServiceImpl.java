package gr.aueb.radio.content.infrastructure.service.user;

import gr.aueb.radio.content.application.UserService;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
    public UserVerifiedRepresentation verifyAuth(String username, String password) {
        String credentials = username + ":" + password;

        // Encode credentials to Base64
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Form Basic Authentication header
        String basicAuthHeaderValue = "Basic " + encodedCredentials;

        UserVerifiedRepresentation user = userApi.verifyAuth(basicAuthHeaderValue);
        System.out.println("returned id "+ user.id);



        return user;
    }
}
