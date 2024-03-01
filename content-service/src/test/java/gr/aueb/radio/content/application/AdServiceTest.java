package gr.aueb.radio.content.application;


import gr.aueb.radio.content.common.DateUtil;
import gr.aueb.radio.content.common.IntegrationBase;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.infrastructure.persistence.AdRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.AdBroadcastBasicRepresentation;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.SongBroadcastBasicRepresentation;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import org.mockito.Mockito;
import jakarta.ws.rs.NotFoundException;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@QuarkusTest
public class AdServiceTest extends IntegrationBase {
    @Inject
    AdService adService;

    @Inject
    AdRepository adRepository;

    @InjectMock
    UserService userService;

    @InjectMock
    BroadcastService broadcastService;


    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "PRODUCER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);
    }

    @Test
    public void findAdTest(){
        List<Ad> ads  = adRepository.listAll();
        Integer adId = ads.get(0).getId();
        AdRepresentation ad = adService.findAd(adId,"PRODUCER");
        assertNotNull(ad);
        assertThrows(NotFoundException.class, () -> adService.findAd(-1, "auth"));
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "USER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);
        assertThrows(RadioException.class, () -> adService.findAd(-1, "auth"));
    }

    @Test
    public void Search(){
        // check retrieval of all ads by timezone
        List<AdRepresentation> adsLateNight  = adService.search( Zone.LateNight, null, "auth");
        for(AdRepresentation ad : adsLateNight) {
            assertEquals(ad.timezone, Zone.LateNight);
        }
        // Mock user verification to return a USER role to test the radio exception
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.role = "USER";
        when(userService.verifyAuth(anyString())).thenReturn(user);
        assertThrows(RadioException.class, () -> adService.search(Zone.LateNight, null,""));
    }

    @Test
    public void CreateAdTest() {

        List<Ad> adsList = adRepository.listAll();
        int numOfAds = adsList.size();
        // create new ad
        AdRepresentation adRepresentation = new AdRepresentation();
        adRepresentation.duration = 60;
        adRepresentation.startingDate = "01-01-2022";
        adRepresentation.endingDate = "01-03-2022";
        adRepresentation.timezone = Zone.EarlyMorning;
        adRepresentation.repPerZone = 2;
        // check list of ads is plus one
        Ad adCreated = adService.create(adRepresentation,"PRODUCER");
        assertEquals(numOfAds +1 , adRepository.listAll().size());
        // check correct creation ex. starting date, timezone
        assertEquals(Zone.EarlyMorning, adCreated.getTimezone());
        LocalDate dateToCheck = DateUtil.setDate(adRepresentation.startingDate);
        assertTrue(dateToCheck.equals(adCreated.getStartingDate()));
        // Mock user verification to return a USER role to test the radio exception
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.role = "USER";
        when(userService.verifyAuth(anyString())).thenReturn(user);
        assertThrows(RadioException.class, () -> adService.create(adRepresentation,"USER"));
    }

    @Test
    public void UpdateAdTest() {
        AdRepresentation adRepresentation = new AdRepresentation();
        adRepresentation.duration = 60;
        adRepresentation.startingDate = "01-01-2022";
        adRepresentation.endingDate = "01-03-2022";
        adRepresentation.timezone = Zone.EarlyMorning;
        adRepresentation.repPerZone = 2;


        adRepresentation.startingDate = "01-02-2022";
        Ad adCreated = adService.create(adRepresentation,"auth");
        LocalDate dateToCheck = DateUtil.setDate(adRepresentation.startingDate);
        int numOfAds = adRepository.listAll().size();
        Ad adUpdated = adService.update(1001, adRepresentation,"auth");
        //  check that num of ads not changed after update
        assertEquals(numOfAds, adRepository.listAll().size());
        // check for correct update
        assertEquals(dateToCheck, adUpdated.getStartingDate());
        // Ad is immutable, it has song/add scheduled
        List<AdBroadcastBasicRepresentation> listOfAb = new ArrayList<>();
        AdBroadcastBasicRepresentation ab = new AdBroadcastBasicRepresentation();
        ab.id = 2;
        listOfAb.add(ab);

        Mockito.when(broadcastService.getAdBroadcastsByAdId(anyString(),anyInt())).thenReturn(listOfAb);

        assertThrows(RadioException.class, () -> {
            adService.update(adCreated.getId(), adRepresentation, "auth");
        });
        // not found
        assertThrows(NotFoundException.class, () -> adService.update(-1, adRepresentation,"auth"));
        // User unauthorised
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.role = "USER";
        when(userService.verifyAuth(anyString())).thenReturn(user);
        assertThrows(RadioException.class, () -> adService.update(adCreated.getId(), adRepresentation,"auth"));



    }

    @Test
    public void DeleteAdTest() {

        List<Ad> adsList = adRepository.listAll();
        Integer adId = adsList.get(0).getId();
        int numOfAds = adsList.size();


        List<AdBroadcastBasicRepresentation> listOfAd = new ArrayList<>();
        AdBroadcastBasicRepresentation adBroadcast = new AdBroadcastBasicRepresentation();
        adBroadcast.id = 1;
        listOfAd.add(adBroadcast);

        Mockito.when(broadcastService.getAdBroadcastsByAdId(anyString(),anyInt())).thenReturn(listOfAd);

        adService.delete(adId,"auth");
        assertEquals(numOfAds - 1, adRepository.listAll().size());
        // not found
        assertThrows(NotFoundException.class, () -> adService.delete(-1,"auth"));
        // User unauthorised
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.role = "USER";
        when(userService.verifyAuth(anyString())).thenReturn(user);
        assertThrows(RadioException.class, () -> adService.delete(adId,"auth"));
    }

}









