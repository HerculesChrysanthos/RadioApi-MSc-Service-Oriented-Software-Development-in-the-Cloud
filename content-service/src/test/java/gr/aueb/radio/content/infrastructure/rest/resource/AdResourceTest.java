package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.Fixture;
import gr.aueb.radio.content.application.AdService;
import gr.aueb.radio.content.application.UserService;
import gr.aueb.radio.content.common.ExternalServiceException;
import gr.aueb.radio.content.common.IntegrationBase;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.ad.Ad;
import gr.aueb.radio.content.domain.ad.Zone;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.persistence.AdRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.*;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@QuarkusTest
class AdResourceTest extends IntegrationBase {
    @Inject
    AdRepository adRepository;

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
    public void testFindAd() {
        // find valid ad in DB to search
        Ad ad = adRepository.listAll().get(0);
        // send get request to find the ad
        String url = Fixture.API_ROOT + Fixture.ADS + "/" + ad.getId();
        AdRepresentation foundAd = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(AdRepresentation.class);
        // verify that the found song matches the original song form db
        assertEquals(foundAd.timezone, ad.getTimezone());
        assertEquals(foundAd.duration, ad.getDuration());
        assertEquals(foundAd.repPerZone, ad.getRepPerZone());

        // try to find a song that does not exist
        url = Fixture.API_ROOT + Fixture.ADS + "/-1";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().get(url)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());


    }

    @Test
    public void tesGetAdExternalServiceException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));

        String url = Fixture.API_ROOT + Fixture.ADS + "/1";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().get(url)
                .then().statusCode(424);
    }

    @Test
    public void tesGetAdRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth error", 403));

        String url = Fixture.API_ROOT + Fixture.ADS + "/1";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().get(url)
                .then().statusCode(403);
    }


    @Test
    void testCreateAd() {
//        AdInputDTO adInputDTO = new AdInputDTO();
        AdRepresentation ad = new AdRepresentation();
        ad.timezone = Zone.EarlyMorning;
        ad.repPerZone = 1;
        ad.duration = 3;
        ad.startingDate = "01-01-2022";
        ad.endingDate = "02-12-2022";
        int numOfAds = adRepository.listAll().size();

        String url = Fixture.API_ROOT + Fixture.ADS;
        AdRepresentation adCreated = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(ad)
                .when().post(url)
                .then().statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(AdRepresentation.class);

        assertNotNull(adCreated);
        assertTrue(ad.repPerZone.equals(adCreated.repPerZone));
        assertTrue(ad.startingDate.equals(adCreated.startingDate));
        assertTrue(ad.endingDate.equals(adCreated.endingDate));
        assertEquals(numOfAds + 1, adRepository.listAll().size());
    }

    @Test
    public void testCreateBadRequest() {

        // test radio exception
        doThrow(new RadioException("Invalid ad input")).when(userService).verifyAuth(any(String.class));
        AdInputDTO ad1 = new AdInputDTO();
        String url = Fixture.API_ROOT + Fixture.ADS;
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(ad1)
                .when().post(url)
                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

        @Test
        public void tesCreateRadioException() {
        // test external service exception
            String url = Fixture.API_ROOT + Fixture.ADS;
        AdInputDTO ad2 = new AdInputDTO();
        ad2.timezone = Zone.EarlyMorning;
        ad2.repPerZone = 1;
        ad2.duration = 3;
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(ad2)
                .when().post(url)
                .then().statusCode(424);
    }

    @Test
    public void searchAdFilteredTest() {
        String url = Fixture.API_ROOT + Fixture.ADS + "/";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .queryParam("adsIds", "1001")
                .when()
                .get(url)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).extract();
    }

    @Test
    public void testSearchWithFiltersRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth error", 403));

        String url = Fixture.API_ROOT + Fixture.ADS + "/";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().get(url)
                .then().statusCode(403);
    }

}


