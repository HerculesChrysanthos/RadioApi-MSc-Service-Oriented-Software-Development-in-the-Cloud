package gr.aueb.radio.services;

import gr.aueb.radio.IntegrationBaseExtended;
import gr.aueb.radio.domains.AdBroadcast;
import gr.aueb.radio.dto.AdBroadcastCreationDTO;
import gr.aueb.radio.persistence.AdBroadcastRepository;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AdBroadcastServiceTest extends IntegrationBaseExtended {
    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    AdBroadcastService adBroadcastService;

    @Inject
    AdRepository adRepository;

    @Test
    @TestTransaction
    public void CreateAdBroadcastTest() {

        List<AdBroadcast> adBroadcastsList = adBroadcastRepository.listAll();
//        Integer adBroadcastId = adBroadcastsList.get(0).getId();
        int numOfAdBroadcasts = adBroadcastsList.size();

        AdBroadcastCreationDTO ab = new AdBroadcastCreationDTO(3002,1035,"08:00");

        AdBroadcast created = adBroadcastService.create(ab);;
        assertEquals(numOfAdBroadcasts + 1, adBroadcastRepository.listAll().size());
        // check correct creation ex. starting date
        LocalTime timeToCheck = DateUtil.setTime(ab.startingTime);
        assertTrue(timeToCheck.equals(created.getBroadcastTime()));

    }

    @Test
    @TestTransaction
    public void findAdTest(){
        List<AdBroadcast> adBroadcastsList = adBroadcastRepository.listAll();
        Integer adBroadcastId = adBroadcastsList.get(0).getId();
        AdBroadcast ab = adBroadcastService.find(adBroadcastId);
        assertNotNull(adBroadcastId);
    }

    @Test
    @TestTransaction
    public void findAdBroadcastDateTest(){
        List<AdBroadcast> adBroadcastsOfDay  = adBroadcastRepository.findByDate(DateUtil.setDate("01-01-2022"));
        for(AdBroadcast adBroadcast : adBroadcastsOfDay) {
            assertEquals(adBroadcast.getBroadcastDate(), DateUtil.setDate("01-01-2022"));
        }
    }

}
