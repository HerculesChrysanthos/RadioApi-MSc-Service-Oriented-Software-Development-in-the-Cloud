package gr.aueb.radio.user.infrastructure.rest.resource.health;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class OSHealthCheck implements HealthCheck {
    @Inject
    @ConfigProperty(name = "freememory.threshold", defaultValue = " 42949672964294967296") // 4gb
    private long threshold;
    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("MemoryHealthCheck Liveness check");
        long freeMemory = Runtime.getRuntime().freeMemory();
        if (freeMemory >= threshold) {
            responseBuilder.up().withData("status", "UP");
        } else {
            responseBuilder.down().withData("error",
                    "Not enough free memory! Available " + freeMemory + "Please restart application");
        }
        return responseBuilder.build();
    }
}
