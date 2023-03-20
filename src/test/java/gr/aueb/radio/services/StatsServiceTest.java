package gr.aueb.radio.services;

import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.dto.AdStatsDTO;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.persistence.BroadcastRepository;
import gr.aueb.radio.representations.BroadcastOutputRepresentation;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.inject.Inject;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mockStatic;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
public class StatsServiceTest extends IntegrationBase {
    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    StatService statisticsService;

    @Test
    public void getProgramTest(){
        LocalDate validDate = broadcastRepository.listAll().get(0).getStartingDate();
        String validDateString = DateUtil.setDateToString(validDate);
        try(MockedStatic dateUtil = mockStatic(DateUtil.class)){
            dateUtil.when(DateUtil::dateNow).thenReturn(validDate);
            List<BroadcastOutputRepresentation> program = statisticsService.getDailySchedule(null);
            assertTrue(program.size() > 0);
            dateUtil.verify(DateUtil::dateNow);
        }

        List<BroadcastOutputRepresentation> program = statisticsService.getDailySchedule(validDateString);
        assertTrue(program.size() > 0);

    }

    @Test
    public void getAdStatsTest(){
        Broadcast b = broadcastRepository.listAll().get(0);
        LocalDate validDate = b.getStartingDate();
        String validDateString = DateUtil.setDateToString(validDate);
        try(MockedStatic dateUtil = mockStatic(DateUtil.class)){
            dateUtil.when(DateUtil::dateNow).thenReturn(validDate);
            AdStatsDTO dto = statisticsService.extractAdStats(null);
            assertNotNull(dto);
            dateUtil.verify(DateUtil::dateNow);
        }

        AdStatsDTO dto = statisticsService.extractAdStats(validDateString);
        assertNotNull(dto);

        int adBroadcastsSize = b.getAdBroadcasts().size();
        assertEquals(adBroadcastsSize, dto.adsPerBroadcastZone.get(b.getType()).size());
        ZoneEnum broadcastTimezone = b.getTimezone();
        assertEquals(adBroadcastsSize, dto.adsPerTimeZone.get(broadcastTimezone).size());
        List<ZoneEnum> timezones = Arrays.asList(ZoneEnum.values());
        List<BroadcastEnum> types = Arrays.asList(BroadcastEnum.values());
        for(ZoneEnum zone : timezones){
            assertNotNull(dto.adsPerTimeZone.get(zone));
        }
        for(BroadcastEnum type : types){
            assertNotNull(dto.adsPerBroadcastZone.get(type));
        }
    }

}
