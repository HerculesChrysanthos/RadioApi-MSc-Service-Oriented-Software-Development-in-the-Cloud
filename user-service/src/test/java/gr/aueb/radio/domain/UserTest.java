package gr.aueb.radio.domain;


import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.domain.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    User userEntity;
    private static String username = "testUser";
    private static String password = "testPassword";
    private static String email = "testUser@email.com";

    @BeforeEach
    public void setup(){
        userEntity = new User("test", "test", "test@email.com",  Role.USER);
    }

    @Test
    public void updateUser(){
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setEmail(email);
        userEntity.setRole(Role.USER);

        assertNotNull(userEntity);
        assertEquals(username, userEntity.getUsername());
        assertEquals(password, userEntity.getPassword());
        assertEquals(email, userEntity.getEmail());
        assertEquals(Role.USER, userEntity.getRole());
    }

    @ParameterizedTest
    @CsvSource({
            "test, test, test@email.com, USER, true",
            "test, test, test1@email.com, USER, false",
            "test1, test, test@email.com, USER, false",
            "test1, test, test1@email.com, USER, false",
    })
    public void equalsUserDataTest(String username, String password, String email, Role role, boolean expectedResult){
        User user = new User(username, password, email,  role);
        assertEquals(expectedResult, userEntity.equals(user));
    }
    @Test
    public void equalsObjectTest(){
        assertTrue(userEntity.equals(userEntity));
        assertFalse(userEntity.equals(null));
        assertFalse(userEntity.equals(String.class));
    }
}