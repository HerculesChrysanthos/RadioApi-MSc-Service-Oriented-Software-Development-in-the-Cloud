package gr.aueb.radio.infrastructure.rest.representation;

import gr.aueb.radio.user.infrastructure.rest.representation.UserBasicRepresentation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserBasicRepresentationTest {

    @Test
    public void testUserBasicRepresentation() {
        // Arrange
        Integer id = 6001;
        String role = "admin";

        // Act
        UserBasicRepresentation user = new UserBasicRepresentation();
        user.id = id;
        user.role = role;

        // Assert
        assertEquals(id, user.id);
        assertEquals(role, user.role);
    }
}