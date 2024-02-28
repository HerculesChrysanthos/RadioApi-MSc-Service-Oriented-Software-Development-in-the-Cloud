package gr.aueb.radio.infrastructure.rest.representation;

import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.rest.representation.UserMapper;
import gr.aueb.radio.user.infrastructure.rest.representation.UserRepresentation;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void testToRepresentation() {
        User user = new User();
        user.setUsername("user");
        UserRepresentation representation = userMapper.toRepresentation(user);
        assertNotNull(representation);
        assertEquals("user", representation.username);
    }

    @Test
    public void testToModel() {
        UserRepresentation representation = new UserRepresentation();
        representation.email = "e@e.com";
        representation.username = "user";
        User user = userMapper.toModel(representation);
        assertNotNull(user);
        assertEquals( "e@e.com", user.getEmail());
        assertEquals("user", user.getUsername());
    }



}
