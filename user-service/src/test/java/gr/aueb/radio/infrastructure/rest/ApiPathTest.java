package gr.aueb.radio.infrastructure.rest;

import gr.aueb.radio.user.infrastructure.rest.ApiPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiPathTest {

    @Test
    public void testRootUri() {
        String expectedUri = "/api";
        String actualUri = ApiPath.ROOT;

        Assertions.assertEquals(expectedUri, actualUri);
    }

    public static class RootTest {
        @Test
        public void testUsersUri() {
            String expectedUri = "/api/users";
            String actualUri = ApiPath.Root.USERS;

            Assertions.assertEquals(expectedUri, actualUri);
        }

        @Test
        public void testApiPath() {
            ApiPath twitterUri = new ApiPath();
            // Call any method on the class to ensure its code is executed
            twitterUri.getClass();
        }

        @Test
        public void testApiPathConstructor() {
            new ApiPath();
        }
    }

}