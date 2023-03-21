package gr.aueb.radio.resources;

import gr.aueb.radio.Fixture;
import gr.aueb.radio.IntegrationBaseExtended;
import gr.aueb.radio.domains.AdBroadcast;
import gr.aueb.radio.dto.AdBroadcastCreationDTO;
import gr.aueb.radio.persistence.AdBroadcastRepository;
import gr.aueb.radio.representations.AdBroadcastRepresentation;
import gr.aueb.radio.services.AdBroadcastService;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class AdBroadcastResourceTest extends IntegrationBaseExtended {

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    AdBroadcastService adBroadcastService;

    @Test
    @TestTransaction
    public void getAdBroadcastTest(){
        AdBroadcast validAb = adBroadcastRepository.listAll().get(0);
        String url = Fixture.API_ROOT + Fixture.ADBROADCAST_PATH + "/" + validAb.getId();

        AdBroadcastRepresentation ab = given().auth().preemptive().basic("producer", "producer")
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(AdBroadcastRepresentation.class);

        assertEquals(validAb.getBroadcastDate(), DateUtil.setDate(ab.broadcastDate));
        assertEquals(validAb.getBroadcastTime(), DateUtil.setTime(ab.broadcastTime));

        String url2 = Fixture.API_ROOT + Fixture.ADBROADCAST_PATH + "/-1";
        given().auth().preemptive().basic("producer", "producer")
                .when()
                .get(url2)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }
    @Test
    @TestTransaction
    public void createAdBrTest(){
        AdBroadcastCreationDTO abInv = new AdBroadcastCreationDTO(3002,-1,"08:00");
        String url = Fixture.API_ROOT + Fixture.ADBROADCAST_PATH;
        int numOfAdBrs = adBroadcastRepository.listAll().size();
        // not found
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .body(abInv)
                .when()
                .post(url)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
        //
        AdBroadcastCreationDTO ab = new AdBroadcastCreationDTO(3001,1035,"08:00");
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .body(ab)
                .when()
                .post(url)
                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        // authorized user with permissions valid adbroadcast
        AdBroadcastCreationDTO abValid = new AdBroadcastCreationDTO(3002,1035,"08:00");
        AdBroadcastRepresentation adBroadcastRepresentation = given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .body(abValid)
                .when()
                .post(url)
                .then().statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(AdBroadcastRepresentation.class);
        assertEquals(ab.startingTime, adBroadcastRepresentation.broadcastTime);
        assertEquals(numOfAdBrs + 1, adBroadcastRepository.listAll().size());
    }

    @Test
    public void deleteAdBroadcastTest() {
        List<AdBroadcast> initialAdBroadcast = adBroadcastRepository.listAll();
        int adId = initialAdBroadcast.get(0).getId();
        int initialNumOfAdBroadcasts = initialAdBroadcast.size();

        String url = Fixture.API_ROOT + Fixture.ADBROADCAST_PATH + "/-1";
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());

        // test successful delete
        String url1 = Fixture.API_ROOT + Fixture.ADBROADCAST_PATH + "/" + adId;
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .when()
                .delete(url1)
                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());

        int finalNumOfAds = adBroadcastRepository.listAll().size();
        assertEquals(initialNumOfAdBroadcasts - 1, finalNumOfAds);
    }

    @Test
    public void getAdBrDate() {
        String date = "01-02-2023";
        int numOfDailyAdBr = adBroadcastRepository.findByDate(DateUtil.setDate(date)).size();
        AdBroadcastCreationDTO a = new AdBroadcastCreationDTO(3002,1035,"08:00");

        // invalid search
        String url = Fixture.API_ROOT + Fixture.ADBROADCAST_PATH ;
         given().auth().preemptive().basic("producer", "producer")
                .queryParam("date", -1)
                .when()
                .get(url)
                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode());


        // create a new adBr with broadcastDate the local var date
        AdBroadcast created = adBroadcastService.create(a);
        List<AdBroadcastRepresentation> found = given().auth().preemptive().basic("producer", "producer")
                .queryParam("date", DateUtil.setDateToString(created.getBroadcastDate()))
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });

        assertEquals(numOfDailyAdBr + 1, found.size());
    }

}
