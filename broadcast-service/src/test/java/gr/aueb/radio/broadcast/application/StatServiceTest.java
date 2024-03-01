package gr.aueb.radio.broadcast.application;

import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import gr.aueb.radio.broadcast.domain.broadcast.Zone;
import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdStatsDTO;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastOutputRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
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
import java.time.LocalTime;
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

    @InjectMock
    ContentService contentService;

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
    private void testExtractFromAdBroadcast() {
        List<AdBroadcast> adBroadcasts = new ArrayList<>();
        // Fill the list with instances of AdBroadcast
        adBroadcasts.add(new AdBroadcast(LocalDate.of(2024, 3, 1), LocalTime.of(8, 30)));
        adBroadcasts.add(new AdBroadcast(LocalDate.of(2024, 3, 2), LocalTime.of(9, 45)));
        adBroadcasts.add(new AdBroadcast(LocalDate.of(2024, 3, 3), LocalTime.of(10, 15)));


        List<AdBasicRepresentation> ab = statService.extractFromAdBroadcast(adBroadcasts, "auth");
        assertNotNull(ab);

    }



    @Test
    public void getAdStatsTest(){
        Broadcast b = broadcastRepository.listAll().get(0);
        LocalDate validDate = b.getStartingDate();
        String validDateString = DateUtil.setDateToString(validDate);

        AdStatsDTO dto = statService.extractAdStats(validDateString, "auth");
        assertNotNull(dto);

        int adBroadcastsSize = b.getAdBroadcasts().size();
        Zone broadcastTimezone = b.getTimezone();

        List<BroadcastType> types = Arrays.asList(BroadcastType.values());

        for(BroadcastType type : types){
            assertNotNull(dto.adsPerBroadcastZone.get(type));
        }
    }

}
