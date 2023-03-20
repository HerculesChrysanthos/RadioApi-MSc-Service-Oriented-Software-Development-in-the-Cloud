package gr.aueb.radio.services;

import gr.aueb.radio.IntegrationBaseExtended;
import gr.aueb.radio.domains.AdBroadcast;
import gr.aueb.radio.dto.AdBroadcastCreationDTO;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.persistence.AdBroadcastRepository;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
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
        int numOfAdBroadcasts = adBroadcastsList.size();

        AdBroadcastCreationDTO abNull = new AdBroadcastCreationDTO(3002,-1,"08:00");
        assertThrows(NotFoundException.class, () -> {
            adBroadcastService.create(abNull);
        });

        AdBroadcastCreationDTO ab = new AdBroadcastCreationDTO(3002,1035,"08:00");

        AdBroadcast created = adBroadcastService.create(ab);
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

    @Test
    @TestTransaction
    public void findAdBrDateTest(){
        String date = "01-02-2023";
        LocalDate dateToSearch = DateUtil.setDate(date);
        adBroadcastService.search(date);
        List<AdBroadcast> AdBroadcastsList = adBroadcastRepository.findByDateDetails(dateToSearch);
        for(AdBroadcast adBroadcast : AdBroadcastsList) {
            assertEquals(adBroadcast.getBroadcastDate(), dateToSearch);
        }
    }

    @Test
    @TestTransaction
    public void DeleteAdBrTest() {

        List<AdBroadcast> adBroadcastsList = adBroadcastRepository.listAll();
        Integer adBroadcastId = adBroadcastsList.get(0).getId();
        int numOfAdBroadcasts = adBroadcastsList.size();

        // adId does to be updated not found
        assertThrows(NotFoundException.class, () -> {
            adBroadcastService.delete(-1);
        });

        adBroadcastService.delete(adBroadcastId);
        assertNotEquals(numOfAdBroadcasts, adRepository.listAll().size());

    }

}
