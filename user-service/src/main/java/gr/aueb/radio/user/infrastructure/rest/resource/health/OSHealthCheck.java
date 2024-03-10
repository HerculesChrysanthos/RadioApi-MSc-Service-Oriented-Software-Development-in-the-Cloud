package gr.aueb.radio.user.infrastructure.rest.resource.health;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

//@Liveness
//@ApplicationScoped
//public class LivenessHealthCheck implements HealthCheck {
//
////    @Inject
////    UriInfo uriInfo;
//
////    @Context
////    UriInfo uriInfo;\
//
//    @Override
//    public HealthCheckResponse call() {
//        // Check if the application is live
//        if(isApplicationLive()) {
//
//            return HealthCheckResponse.up("Application is live");
//        }
//        return HealthCheckResponse.up("Application is not live");
//    }
//
//    public boolean isApplicationLive() {
//       // String endpoint = uriInfo.getPath();// + "/api/check-auth" ;
//
//        String relativePath = uriInfo.getPath();
//
//        String e = "https:localhost:8080/api/check-auth";
//
////        try {
////            URL url = new URL(e);
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("GET");
////            int responseCode = connection.getResponseCode();
////            return responseCode == 401;
////        } catch (IOException er) {
////            // Log or handle the exception if necessary
////            return false;
////        }
//        return false;
//    }
//
//}


// TODO alternative : create user client and call it
@Liveness
@ApplicationScoped
public class OSHealthCheck implements HealthCheck {
    @Inject
    @ConfigProperty(name = "freememory.threshold", defaultValue = "10485760")
    private long threshold;
    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("MemoryHealthCheck Liveness check");
        long freeMemory = Runtime.getRuntime().freeMemory();
        if (freeMemory >= threshold) {
            responseBuilder.up();
        } else {
            responseBuilder.down().withData("error",
                    "Not enough free memory! Available " + freeMemory + "Please restart application");
        }
        return responseBuilder.build();
    }
}
