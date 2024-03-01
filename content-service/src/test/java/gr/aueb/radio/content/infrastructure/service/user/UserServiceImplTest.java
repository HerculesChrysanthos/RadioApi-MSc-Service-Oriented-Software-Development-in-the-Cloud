package gr.aueb.radio.content.infrastructure.service.user;

import gr.aueb.radio.content.common.ExternalServiceException;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.ws.rs.ProcessingException;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceImplTest {

    @Mock
    private Logger mockLogger;
    @Mock
    private UserApi mockUserApi;

    private UserServiceImpl userServiceImplUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        userServiceImplUnderTest = new UserServiceImpl();
        userServiceImplUnderTest.logger = mockLogger;
        userServiceImplUnderTest.userApi = mockUserApi;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testVerifyAuth() {
        when(mockUserApi.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());

        UserVerifiedRepresentation result = userServiceImplUnderTest.verifyAuth("auth");

        assertNotNull(result);
    }

    @Test
    void testVerifyThrowsError() {
        when(mockUserApi.verifyAuth("auth")).thenThrow(new ProcessingException(""));

        assertThrows(ExternalServiceException.class, () -> userServiceImplUnderTest.verifyAuth("auth"));
    }
}
