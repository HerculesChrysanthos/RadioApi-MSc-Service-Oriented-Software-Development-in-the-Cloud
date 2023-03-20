package gr.aueb.radio.resources;

import gr.aueb.radio.Fixture;
import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.domains.Ad;
import gr.aueb.radio.enums.ZoneEnum;
import gr.aueb.radio.persistence.AdRepository;
import gr.aueb.radio.representations.AdRepresentation;
import gr.aueb.radio.services.AdService;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class AdResourceTest extends IntegrationBase {
    @Inject
    AdRepository adRepository;

    @Inject
    AdService adService;

    @Test
    @TestTransaction
    public void getAdTest(){
        Ad validAd = adRepository.listAll().get(0);
        String url = Fixture.API_ROOT + Fixture.ADS_PATH + "/" + validAd.getId();
        AdRepresentation adRepresentation = when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(AdRepresentation.class);

        assertEquals(validAd.getDuration(), adRepresentation.duration);
        assertEquals(validAd.getStartingDate(), DateUtil.setDate(adRepresentation.startingDate));
        assertEquals(validAd.getEndingDate(), DateUtil.setDate(adRepresentation.endingDate));
        assertEquals(validAd.getTimezone(), adRepresentation.timezone);
        assertEquals(validAd.getRepPerZone(), adRepresentation.repPerZone);

        String url2 = Fixture.API_ROOT + Fixture.ADS_PATH + "/-1";
        when()
                .get(url2)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }


    @Test
    @TestTransaction
    public void createAdTest(){
        AdRepresentation a = new AdRepresentation();
        a.duration = 60;
        a.startingDate = "01-01-2022";
        a.endingDate = "01-03-2022";
        a.timezone = ZoneEnum.EarlyMorning;
        a.repPerZone = 2;
        String url = Fixture.API_ROOT + Fixture.ADS_PATH;
        int numOfAds = adRepository.listAll().size();
        // authorized user with permissions valid broadcast
       AdRepresentation adRepresentation = given()
                .contentType(ContentType.JSON)
                .body(a)
                .when()
                .post(url)
                .then().statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(AdRepresentation.class);
//        assertEquals(a.type, adRepresentation1.type);
        assertEquals(a.duration, adRepresentation.duration);
        assertTrue(a.repPerZone.equals(adRepresentation.repPerZone));
        assertTrue(a.startingDate.equals(adRepresentation.startingDate));
        assertTrue(a.endingDate.equals(adRepresentation.endingDate));
        assertEquals(numOfAds + 1, adRepository.listAll().size());
    }

    @Test
    public void updateAdTest(){
        AdRepresentation a = new AdRepresentation();
        a.duration = 60;
        a.startingDate = "01-01-2022";
        a.endingDate = "01-03-2022";
        a.timezone = ZoneEnum.EarlyMorning;
        a.repPerZone = 2;

        // create a new ad to be updated
        Ad created = adService.create(a);
        // provide updated values for ad
        a.repPerZone = 1;
        a.startingDate = "01-02-2022";
        LocalDate dateToCheck = DateUtil.setDate(a.startingDate);
        String url = Fixture.API_ROOT + Fixture.ADS_PATH+ "/" + created.getId();

         given().contentType(ContentType.JSON)
                .body(a)
                .when()
                .put(url)
                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());

        Ad updated = adRepository.findById(created.getId());
        assertEquals(a.repPerZone, updated.getRepPerZone());
        assertTrue(dateToCheck.equals(updated.getStartingDate()));

        String url2 = Fixture.API_ROOT + Fixture.ADS_PATH + "/-1";
        given().contentType(ContentType.JSON)
                .body(a)
                .when()
                .put(url2)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
        // Test existing broadcast restriction - 1001 already broadcasted
        String url3 = Fixture.API_ROOT + Fixture.ADS_PATH + "/1001";
        given().contentType(ContentType.JSON)
                .body(a)
                .when()
                .put(url3)
                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }
    @Test
    public void getAdTimezoneTest() {
        int numOfAfternoonAds = adRepository.findByTimezone(ZoneEnum.Afternoon).size();
        AdRepresentation a = new AdRepresentation();
        a.duration = 60;
        a.startingDate = "01-01-2022";
        a.endingDate = "01-03-2022";
        a.timezone = ZoneEnum.Afternoon;
        a.repPerZone = 2;

        // create a new ad with timezone Afternoon
        Ad created = adService.create(a);
        String url = Fixture.API_ROOT + Fixture.ADS_PATH ;
        List<AdRepresentation> found = given()
                .queryParam("timezone",(created.getTimezone()))
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });

        assertEquals(numOfAfternoonAds + 1, found.size());
    }

    @Test
    public void deleteAdTest() {
        List<Ad> initialAds = adRepository.listAll();
        int adId = initialAds.get(0).getId();
        int initialNumOfAds = initialAds.size();

        String url = Fixture.API_ROOT + Fixture.ADS_PATH + "/-1";
        given().contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());

        // test successful delete
        String url1 = Fixture.API_ROOT + Fixture.ADS_PATH + "/" + adId;
        given().contentType(ContentType.JSON)
                .when()
                .delete(url1)
                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());

        int finalNumOfAds = adRepository.listAll().size();
        assertEquals(initialNumOfAds - 1, finalNumOfAds);
    }
}
