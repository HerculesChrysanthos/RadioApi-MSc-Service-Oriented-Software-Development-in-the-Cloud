package gr.aueb.radio.content.infrastructure.service.user;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
@ApplicationScoped
public class UserApiRequestInterceptor implements ClientRequestFilter {

    @Inject
    Logger logger;

    @Override
    public void filter(ClientRequestContext requestContext) {
        String url = requestContext.getUri().toString();
        logger.infof("Calling user API with URL: %s", url);
    }
}