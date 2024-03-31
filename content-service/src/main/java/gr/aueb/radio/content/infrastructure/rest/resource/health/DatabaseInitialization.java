package gr.aueb.radio.content.infrastructure.rest.resource.health;

import gr.aueb.radio.content.infrastructure.persistence.SongRepository;
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
    SongRepository songRepository;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Startup check");

        if(songRepository.findById(-1) == null){
            return responseBuilder.up().withData("status", "UP").build();
        }

        return responseBuilder.down().withData("status", "DOWN").build();
    }
}
