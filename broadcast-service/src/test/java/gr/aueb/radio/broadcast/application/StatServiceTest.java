package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdStatsDTO;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastOutputRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@QuarkusTest
public class StatServiceTest {

    @Inject
    BroadcastRepository broadcastRepository;

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

    @Test
    public void testGetDailySchedule_Date() {
        String  date  ="01-01-2022";
        List<BroadcastOutputRepresentation> actualScheduleGivenDate = statService.getDailySchedule(date, "auth");
        assertNotNull(actualScheduleGivenDate);

        List<BroadcastOutputRepresentation> actualSchedule = statService.getDailySchedule(null, "auth");
        assertNotNull(actualSchedule);
    }
    @Test
    public void testExtractAdStats() {
        String date = "2024-02-29";
        AdStatsDTO adStatsDTO = statService.extractAdStats(date, "auth");
        assertNotNull(adStatsDTO);

    }

}
