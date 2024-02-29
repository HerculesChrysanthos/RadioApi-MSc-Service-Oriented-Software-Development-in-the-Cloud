package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastOutputRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@QuarkusTest
public class StatServiceTest {

    @Inject
    BroadcastService broadcastService;

    @Inject
    StatService statService;

    @InjectMock
    UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "PRODUCER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);

    }
    @Test
    public void testGetDailySchedule(){
        BroadcastOutputRepresentation br = new BroadcastOutputRepresentation();
        String dateToSearch = "01-01-2022";
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.role = "USER";
        when(userService.verifyAuth(anyString())).thenReturn(user);
        assertThrows(RadioException.class, () -> statService.getDailySchedule( dateToSearch ,"USER"));
    }

}
