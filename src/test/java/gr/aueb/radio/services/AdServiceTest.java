package gr.aueb.radio.services;

import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.exceptions.NotFoundException;
import gr.aueb.radio.exceptions.RadioException;
import gr.aueb.radio.mappers.AdMapper;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.representations.AdRepresentation;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
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


    @Test
    @TestTransaction
    public void findAdTest(){
        List<Ad> ads  = adRepository.listAll();
        Integer adId = ads.get(0).getId();
        AdRepresentation ad = adService.findAd(adId);
        assertNotNull(ad);
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
        adRepresentation.startingDate = "01-01-2022";
        adRepresentation.endingDate = "01-03-2022";
        adRepresentation.timezone = ZoneEnum.EarlyMorning;
        adRepresentation.repPerZone = 2;
        // check list of ads is plus one
        Ad adCreated = adService.create(adRepresentation);;
        assertEquals(numOfAds + 1, adRepository.listAll().size());
        // check correct creation ex. starting date, timezone
        LocalDate dateToCheck = DateUtil.setDate(adRepresentation.startingDate);
        assertTrue(dateToCheck.equals(adCreated.getStartingDate()));
        assertEquals(ZoneEnum.EarlyMorning, adCreated.getTimezone());
    }

    @Test
    @TestTransaction
    public void UpdateAdTest() {
        AdRepresentation adRepresentation = new AdRepresentation();
        adRepresentation.duration = 60;
        adRepresentation.startingDate = "01-01-2022";
        adRepresentation.endingDate = "01-03-2022";
        adRepresentation.timezone = ZoneEnum.EarlyMorning;
        adRepresentation.repPerZone = 2;

        // adId does to be updated not found
        assertThrows(NotFoundException.class, () -> {
            adService.update(-1, adRepresentation);
        });

        adRepresentation.startingDate = "01-02-2022";
        Ad adCreated = adService.create(adRepresentation);
        LocalDate dateToCheck = DateUtil.setDate(adRepresentation.startingDate);
        int numOfAds = adRepository.listAll().size();
        Ad adUpdated = adService.update(adCreated.getId(), adRepresentation);
        //  check that num of ads not changed after update
        assertEquals(numOfAds, adRepository.listAll().size());
        // check for correct update
        assertTrue(dateToCheck.equals(adUpdated.getStartingDate()));

        // Ad is immutable, it has song/add scheduled
        assertThrows(RadioException.class, () -> {
            adService.update(1001, adRepresentation);
        });
    }

    @Test
    @TestTransaction
    public void DeleteAdTest() {

        List<Ad> adsList = adRepository.listAll();
        Integer adId = adsList.get(0).getId();
        int numOfAds = adsList.size();

        // adId does to be updated not found
        assertThrows(NotFoundException.class, () -> {
            adService.delete(-1);
        });

        adService.delete(adId);
        assertNotEquals(numOfAds, adRepository.listAll().size());

    }


}









