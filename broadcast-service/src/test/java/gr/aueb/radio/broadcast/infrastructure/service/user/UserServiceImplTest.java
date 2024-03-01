package gr.aueb.radio.broadcast.infrastructure.service.user;

import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserServiceImplTest {

    @Mock
    private UserApi mockUserApi;

    private UserServiceImpl userServiceImpl;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        userServiceImpl = new UserServiceImpl();
        userServiceImpl.userApi = mockUserApi;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    public void testVerifyAuth() {
        when(mockUserApi.verifyAuth("auth")).thenReturn(new UserVerifiedRepresentation());

        UserVerifiedRepresentation result = userServiceImpl.verifyAuth("auth");

        assertNotNull(result);
    }

    @Test
    public void testGetAdProcessingException() {
        when(mockUserApi.verifyAuth("auth")).thenThrow(new ProcessingException(""));
        assertThrows(ExternalServiceException.class, () -> userServiceImpl.verifyAuth("auth"));
    }

    @Test
    public void testGetAdWebApplicationException() {
        when(mockUserApi.verifyAuth("auth")).thenThrow(new WebApplicationException(""));
        assertThrows(RadioException.class, () -> userServiceImpl.verifyAuth("auth"));
    }

}
