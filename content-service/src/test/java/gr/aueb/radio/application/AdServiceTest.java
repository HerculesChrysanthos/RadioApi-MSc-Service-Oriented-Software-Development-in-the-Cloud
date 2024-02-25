package gr.aueb.radio.application;

import gr.aueb.radio.content.application.AdService;
import gr.aueb.radio.content.common.DateUtil;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.infrastructure.persistence.AdRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.AdMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
import gr.aueb.radio.infrastructure.IntegrationBase;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AdServiceTest extends IntegrationBase {
    @Inject
    AdService adService;

    @Inject
    AdRepository adRepository;

    @Inject
    AdMapper adMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Test
    @TestTransaction
    public void findAdTest(){
        List<Ad> ads  = adRepository.listAll();
        Integer adId = ads.get(0).getId();
        AdRepresentation ad = adService.findAd(1001,"PRODUCER");
        assertNotNull(ad);
    }

    @Test
    @TestTransaction
    public void findAd(){
        try {
            AdRepresentation expectedValue = null;
            Integer id=0;
            String auth="";


            AdService adservice  =new AdService();
            AdRepresentation actualValue=adservice.findAd( id ,auth);
            System.out.println("Expected Value="+ expectedValue +" . Actual Value="+actualValue);
            assertEquals(expectedValue, actualValue);
        } catch (Exception exception) {

            exception.printStackTrace();
            assertFalse(false);
        }
    }

    @Test
    @TestTransaction
    public void findAdsTimezoneTest(){
        // check retrieval of all ads if timezone null
        List<Ad> ads  = adRepository.listAll();
        int ttlAds = ads.size();
        List<AdRepresentation> test1 = adService.search(null,"PRODUCER");
        int Test = test1.size();
        assertEquals(Test,ttlAds);

        List<Ad> adsLateNight  = adRepository.findByTimezone(Zone.LateNight);
        for(Ad ad : adsLateNight) {
            assertEquals(ad.getTimezone(), Zone.LateNight);
        }
    }

    @Test
    @TestTransaction
    public void CreateAdTest() {

        List<Ad> adsList = adRepository.listAll();
        Integer adId = adsList.get(0).getId();
        int numOfAds = adsList.size();
        // create new ad
        AdRepresentation adRepresentation = new AdRepresentation();
        adRepresentation.duration = 60;
//        adRepresentation.startingDate = "01-01-2022";
//        adRepresentation.endingDate = "01-03-2022";
        adRepresentation.timezone = Zone.EarlyMorning;
        adRepresentation.repPerZone = 2;
        // check list of ads is plus one
        Ad adCreated = adService.create(adRepresentation,"PRODUCER");
        assertEquals(numOfAds + 1, adRepository.listAll().size());
        // check correct creation ex. starting date, timezone
        LocalDate dateToCheck = DateUtil.setDate(adRepresentation.startingDate);
        assertTrue(dateToCheck.equals(adCreated.getStartingDate()));
        assertEquals(Zone.EarlyMorning, adCreated.getTimezone());
    }

    @Test
    @TestTransaction
    public void UpdateAdTest() {
        AdRepresentation adRepresentation = new AdRepresentation();
        adRepresentation.duration = 60;
        adRepresentation.startingDate = "01-01-2022";
        adRepresentation.endingDate = "01-03-2022";
        adRepresentation.timezone = Zone.EarlyMorning;
        adRepresentation.repPerZone = 2;

        // adId does to be updated not found
        assertThrows(RadioException.class, () -> {
            adService.update(-1, adRepresentation,"PRODUCER");
        });

        adRepresentation.startingDate = "01-02-2022";
        Ad adCreated = adService.create(adRepresentation,"PRODUCER");
        LocalDate dateToCheck = DateUtil.setDate(adRepresentation.startingDate);
        int numOfAds = adRepository.listAll().size();
        Ad adUpdated = adService.update(adCreated.getId(), adRepresentation,"PRODUCER");
        //  check that num of ads not changed after update
        assertEquals(numOfAds, adRepository.listAll().size());
        // check for correct update
        assertTrue(dateToCheck.equals(adUpdated.getStartingDate()));

        // Ad is immutable, it has song/add scheduled
        assertThrows(RadioException.class, () -> {
            adService.update(1001, adRepresentation,"PRODUCER");
        });
    }

    @Test
    @TestTransaction
    public void DeleteAdTest() {

        List<Ad> adsList = adRepository.listAll();
        Integer adId = adsList.get(0).getId();
        int numOfAds = adsList.size();

        // adId does to be updated not found
        assertThrows(RadioException.class, () -> {
            adService.delete(-1,"PRODUCER");
        });

        adService.delete(adId,"PRODUCER");
        assertNotEquals(numOfAds, adRepository.listAll().size());

    }


}
