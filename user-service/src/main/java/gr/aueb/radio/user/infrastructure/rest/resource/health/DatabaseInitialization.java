package gr.aueb.radio.user.infrastructure.rest.resource.health;

import gr.aueb.radio.user.infrastructure.persistence.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Startup;

@ApplicationScoped
@Startup
public class DatabaseInitialization implements HealthCheck {

    @Inject
    UserRepository userRepository;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Startup check");

        if(userRepository.findByUsername("test") == null){
            return responseBuilder.up().withData("status", "UP").build();

            //return HealthCheckResponse.up("Startup logic executed successfully").withData("status", "UP");
        }

        return responseBuilder.down().withData("status", "DOWN").build();
    }
}