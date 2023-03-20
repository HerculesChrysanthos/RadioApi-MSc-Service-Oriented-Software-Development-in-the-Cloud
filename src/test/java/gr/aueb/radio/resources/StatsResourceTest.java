package gr.aueb.radio.resources;

import gr.aueb.radio.Fixture;
import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.dto.AdStatsDTO;
import gr.aueb.radio.persistence.BroadcastRepository;
import gr.aueb.radio.representations.BroadcastOutputRepresentation;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
public class StatsResourceTest extends IntegrationBase {

    @Inject
    BroadcastRepository broadcastRepository;
    @Test
    public void getProgramTest(){
        String url = Fixture.API_ROOT + Fixture.STATISTICS_PATH + "/program";
        LocalDate validDate = broadcastRepository.listAll().get(0).getStartingDate();
        String validDateString = DateUtil.setDateToString(validDate);
        // not authorized user
        given().contentType(ContentType.JSON)
                .when()
                .get(url)
                .then().statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
        // authorized user without permissions
        given().auth().preemptive().basic("user", "user")
                .contentType(ContentType.JSON)
                .when()
                .get(url)
                .then().statusCode(Response.Status.FORBIDDEN.getStatusCode());
        // authorized user with permissions
        List<BroadcastOutputRepresentation> broadcastRepresentation = given().auth().preemptive().basic("producer", "producer")
                .queryParam("date",validDateString)
                .contentType(ContentType.JSON)
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<List<BroadcastOutputRepresentation>>() {
                });
        assertTrue(broadcastRepresentation.size() > 0);
    }

    @Test
    public void getAdsStatsTest(){
        String url = Fixture.API_ROOT + Fixture.STATISTICS_PATH + "/ads";
        LocalDate validDate = broadcastRepository.listAll().get(0).getStartingDate();
        String validDateString = DateUtil.setDateToString(validDate);
        // not authorized user
        given().contentType(ContentType.JSON)
                .when()
                .get(url)
                .then().statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
        // authorized user without permissions
        given().auth().preemptive().basic("user", "user")
                .contentType(ContentType.JSON)
                .when()
                .get(url)
                .then().statusCode(Response.Status.FORBIDDEN.getStatusCode());
        // authorized user with permissions
        AdStatsDTO dto = given().auth().preemptive().basic("producer", "producer")
                .queryParam("date",validDateString)
                .contentType(ContentType.JSON)
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(AdStatsDTO.class);
        assertNotNull(dto);
    }
}
