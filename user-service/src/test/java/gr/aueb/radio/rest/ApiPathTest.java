package gr.aueb.radio.rest;

import gr.aueb.radio.user.infrastructure.rest.ApiPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiPathTest {

    @Test
    public void testApiPath() {
        // Arrange
        String expectedRoot = "/api";
        String expectedUsers = "/api/users";

        // Act
        String actualRoot = ApiPath.ROOT;
        String actualUsers = ApiPath.Root.USERS;

        // Assert
        assertEquals(expectedRoot, actualRoot);
        assertEquals(expectedUsers, actualUsers);
    }
}