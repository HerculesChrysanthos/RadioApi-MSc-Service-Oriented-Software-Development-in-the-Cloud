package gr.aueb.radio.application;


import gr.aueb.radio.user.application.UserService;
import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.persistence.UserRepository;
import jakarta.inject.Inject;

public class UserServiceTest {


    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;
    User userEntity;

//    @Test
//    @Transactional
//    public void testCreateUser_WithExistingEmail() {
//        // Given
//        UserRepresentation existingUserRepresentation = new UserRepresentation();
//        existingUserRepresentation.setUsername("existingUser");
//        existingUserRepresentation.setEmail("existing@example.com");
//        userService.create(existingUserRepresentation); // Create an existing user with the email
//
//        UserRepresentation userRepresentation = new UserRepresentation();
//        userRepresentation.setUsername("testuser");
//        userRepresentation.setEmail("existing@example.com"); // Attempt to create a user with the same email
//
//        // When / Then
//        assertThrows(RadioException.class, () -> userService.create(userRepresentation));
//    }

}