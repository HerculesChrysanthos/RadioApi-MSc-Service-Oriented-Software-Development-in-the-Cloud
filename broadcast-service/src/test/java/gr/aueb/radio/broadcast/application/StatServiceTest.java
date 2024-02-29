package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import gr.aueb.radio.broadcast.domain.broadcast.Zone;
import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdStatsDTO;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastOutputRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;


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
        String date = "02-02-2022";
        AdStatsDTO adStatsDTO = statService.extractAdStats(date, "auth");
        assertNotNull(adStatsDTO);

    }



    @Test
    public void getAdStatsTest(){
        Broadcast b = broadcastRepository.listAll().get(0);
        LocalDate validDate = b.getStartingDate();
        String validDateString = DateUtil.setDateToString(validDate);
        try(MockedStatic dateUtil = mockStatic(DateUtil.class)){
            dateUtil.when(DateUtil::dateNow).thenReturn(validDate);
            AdStatsDTO dto = statService.extractAdStats(null, "auth");
            assertNotNull(dto);
            dateUtil.verify(DateUtil::dateNow);
        }

        AdStatsDTO dto = statService.extractAdStats(validDateString, "auth");
        assertNotNull(dto);

        int adBroadcastsSize = b.getAdBroadcasts().size();
        assertEquals(adBroadcastsSize, dto.adsPerBroadcastZone.get(b.getType()).size());
        Zone broadcastTimezone = b.getTimezone();
        assertEquals(adBroadcastsSize, dto.adsPerTimeZone.get(broadcastTimezone).size());
        List<Zone> timezones = Arrays.asList(Zone.values());
        List<BroadcastType> types = Arrays.asList(BroadcastType.values());
        for(Zone zone : timezones){
            assertNotNull(dto.adsPerTimeZone.get(zone));
        }
        for(BroadcastType type : types){
            assertNotNull(dto.adsPerBroadcastZone.get(type));
        }
    }

}
