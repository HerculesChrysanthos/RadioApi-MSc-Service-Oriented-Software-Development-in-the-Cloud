package gr.aueb.radio.application;

import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.user.application.UserService;
import gr.aueb.radio.user.common.NotFoundException;
import gr.aueb.radio.user.common.RadioException;
import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.persistence.UserRepository;
import gr.aueb.radio.user.infrastructure.rest.representation.UserBasicRepresentation;
import gr.aueb.radio.user.infrastructure.rest.representation.UserRepresentation;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UserServiceTest extends IntegrationBase {

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    @ParameterizedTest
    @CsvSource({
            "test, test, user@email.com",
            "user, test, test@email.com",
    })
    @TestTransaction
    public void invalidUserCreationTest(String username, String password, String email) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.username = username;
        userRepresentation.email = email;
        userRepresentation.password = password;
        assertThrows(RadioException.class, () -> {
            userService.create(userRepresentation);
        });
    }

    @Test
    @TestTransaction
    public void userCreationTest() {
        List<User> users = userRepository.listAll();
        int size = users.size();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.username = "test";
        userRepresentation.email = "password";
        userRepresentation.password = "email@email.com";
        assertDoesNotThrow(() -> {
            userService.create(userRepresentation);
        });
        users = userRepository.listAll();
        assertEquals(size + 1, users.size());
    }

    @Test
    @TestTransaction
    public void findInvalidUserTest() {
        assertThrows(NotFoundException.class, () -> {
            userService.findUser(9999);
        });
    }

    @Test
    @TestTransaction
    public void findUserTest() {
        List<User> users = userRepository.listAll();
        Integer validId = users.get(0).getId();

        UserRepresentation foundUser = userService.findUser(validId);
        assertNotNull(foundUser);
    }

    @Test
    @TestTransaction
    public void UserNotFoundTest() {
        // Given
        String username = "test_username";
        User user = new User();
        user.setUsername(username);

        // Then
        assertThrows(NotFoundException.class, () -> {
            userService.findUserByUsername(username);
            // Add additional assertions if needed
        });
    }

    @Test
    @TestTransaction
    public void UserFoundByNameTest() {
        // Given
        List<User> users = userRepository.listAll();
        String validname = users.get(0).getUsername();

        UserBasicRepresentation foundUserByName = userService.findUserByUsername(validname);

        assertNotNull(foundUserByName);
    }


}