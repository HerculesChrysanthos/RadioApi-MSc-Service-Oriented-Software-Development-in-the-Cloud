//package gr.aueb.radio.infrastructure.resource;
//
//import gr.aueb.radio.content.application.AdService;
//import gr.aueb.radio.content.common.DateUtil;
//import gr.aueb.radio.content.domain.ad.Ad;
//import gr.aueb.radio.content.domain.ad.Zone;
//import gr.aueb.radio.content.infrastructure.persistence.AdRepository;
//import gr.aueb.radio.content.infrastructure.rest.representation.AdRepresentation;
//import gr.aueb.radio.infrastructure.Fixture;
//import gr.aueb.radio.infrastructure.IntegrationBase;
//import io.quarkus.test.TestTransaction;
//import io.restassured.common.mapper.TypeRef;
//import io.restassured.http.ContentType;
//import jakarta.inject.Inject;
//import jakarta.ws.rs.core.Response;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class AdResourceTest  extends IntegrationBase {
//
//    @Inject
//    AdRepository adRepository;
//
//    @Inject
//    AdService adService;
//
//    @Test
//    @TestTransaction
//    public void getAdTest(){
//        Ad validAd = adRepository.listAll().get(0);
//        String url = Fixture.API_ROOT + Fixture.ADS+ "/" + validAd.getId();
//        System.out.println("url :"+url);
//        AdRepresentation adRepresentation = given().auth().preemptive().basic("producer", "producer")
//                .when()
//                .get(url)
//                .then().statusCode(Response.Status.OK.getStatusCode())
//                .extract().as(AdRepresentation.class);
//
//        assertEquals(validAd.getDuration(), adRepresentation.duration);
//        assertEquals(validAd.getStartingDate(), DateUtil.setDate(adRepresentation.startingDate));
//        assertEquals(validAd.getEndingDate(), DateUtil.setDate(adRepresentation.endingDate));
//        assertEquals(validAd.getTimezone(), adRepresentation.timezone);
//        assertEquals(validAd.getRepPerZone(), adRepresentation.repPerZone);
//
//        String url2 = Fixture.API_ROOT + Fixture.ADS + "/-1";
//        given().auth().preemptive().basic("producer", "producer")
//                .when()
//                .get(url2)
//                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
//
//    }
//
//
//    @Test
//    @TestTransaction
//    public void createAdTest(){
//        AdRepresentation a = new AdRepresentation();
//        a.duration = 60;
//        a.startingDate = "01-01-2022";
//        a.endingDate = "01-03-2022";
//        a.timezone = Zone.EarlyMorning;
//        a.repPerZone = 2;
//        String url = Fixture.API_ROOT + Fixture.ADS;
//        int numOfAds = adRepository.listAll().size();
//        // authorized user with permissions valid broadcast
//        AdRepresentation adRepresentation = given().auth().preemptive().basic("producer", "producer")
//                .contentType(ContentType.JSON)
//                .body(a)
//                .when()
//                .post(url)
//                .then().statusCode(Response.Status.CREATED.getStatusCode())
//                .extract().as(AdRepresentation.class);
//
//        assertEquals(a.duration, adRepresentation.duration);
//        assertTrue(a.repPerZone.equals(adRepresentation.repPerZone));
//        assertTrue(a.startingDate.equals(adRepresentation.startingDate));
//        assertTrue(a.endingDate.equals(adRepresentation.endingDate));
//        assertEquals(numOfAds + 1, adRepository.listAll().size());
//
//        // trigger a radio exception with bad date format
//        a.endingDate = "2022";
//        given().auth().preemptive().basic("producer", "producer")
//                .contentType(ContentType.JSON)
//                .body(a)
//                .when()
//                .post(url)
//                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode());
//    }
//
//    @Test
//    public void updateAdTest(){
//        AdRepresentation a = new AdRepresentation();
//        a.duration = 60;
//        a.startingDate = "01-01-2022";
//        a.endingDate = "01-03-2022";
//        a.timezone =Zone.EarlyMorning;
//        a.repPerZone = 2;
//
//        // create a new ad to be updated
//        Ad created = adService.create(a,"PRODUCER");
//        // provide updated values for ad
//        a.repPerZone = 1;
//        a.startingDate = "01-02-2022";
//        LocalDate dateToCheck = DateUtil.setDate(a.startingDate);
//        String url = Fixture.API_ROOT + Fixture.ADS+ "/" + created.getId();
//
//        given().auth().preemptive().basic("producer", "producer").contentType(ContentType.JSON)
//                .body(a)
//                .when()
//                .put(url)
//                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());
//
//        Ad updated = adRepository.findById(created.getId());
//        assertEquals(a.repPerZone, updated.getRepPerZone());
//        assertTrue(dateToCheck.equals(updated.getStartingDate()));
//
//        String url2 = Fixture.API_ROOT + Fixture.ADS + "/-1";
//        given().auth().preemptive().basic("producer", "producer").contentType(ContentType.JSON)
//                .body(a)
//                .when()
//                .put(url2)
//                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
//        // Test existing broadcast restriction - 1001 already broadcasted
//        String url3 = Fixture.API_ROOT + Fixture.ADS + "/1001";
//        given().auth().preemptive().basic("producer", "producer").contentType(ContentType.JSON)
//                .body(a)
//                .when()
//                .put(url3)
//                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode());
//    }
//    @Test
//    public void getAdTimezoneTest() {
//        int numOfAfternoonAds = adRepository.findByTimezone(Zone.Afternoon).size();
//        AdRepresentation a = new AdRepresentation();
//        a.duration = 60;
//        a.repPerZone = 2;
//        a.startingDate = "01-01-2022";
//        a.endingDate = "01-03-2022";
//        a.timezone = Zone.Afternoon;
//
//
//        // create a new ad with timezone Afternoon
//        Ad created = adService.create(a,"PRODUCER");
//        String url = Fixture.API_ROOT + Fixture.ADS ;
//        List<AdRepresentation> found = given().auth().preemptive().basic("producer", "producer")
//                .queryParam("timezone",(created.getTimezone()))
//                .when()
//                .get(url)
//                .then().statusCode(Response.Status.OK.getStatusCode())
//                .extract().as(new TypeRef<>() {
//                });
//
//        assertEquals(numOfAfternoonAds + 1, found.size());
//    }
//
//    @Test
//    public void deleteAdTest() {
//        List<Ad> initialAds = adRepository.listAll();
//        int adId = initialAds.get(0).getId();
//        int initialNumOfAds = initialAds.size();
//
//        String url = Fixture.API_ROOT + Fixture.ADS + "/-1";
//        given().auth().preemptive().basic("producer", "producer").contentType(ContentType.JSON)
//                .when()
//                .delete(url)
//                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
//
//        // test successful delete
//        String url1 = Fixture.API_ROOT + Fixture.ADS + "/" + adId;
//        given().auth().preemptive().basic("producer", "producer").contentType(ContentType.JSON)
//                .when()
//                .delete(url1)
//                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());
//
//        int finalNumOfAds = adRepository.listAll().size();
//        assertEquals(initialNumOfAds - 1, finalNumOfAds);
//    }
//}
