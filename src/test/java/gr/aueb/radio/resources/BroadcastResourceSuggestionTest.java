package gr.aueb.radio.resources;

import gr.aueb.radio.Fixture;
import gr.aueb.radio.IntegrationBaseExtended;
import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.dto.SuggestionsDTO;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.persistence.BroadcastRepository;
import gr.aueb.radio.representations.BroadcastRepresentation;
import gr.aueb.radio.services.BroadcastService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BroadcastResourceSuggestionTest extends IntegrationBaseExtended {

    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    BroadcastService broadcastService;

    private BroadcastRepresentation createRepresentation(){
        BroadcastRepresentation broadcastRepresentation = new BroadcastRepresentation();
        broadcastRepresentation.duration = 45;
        broadcastRepresentation.startingDate = "11-02-2023";
        broadcastRepresentation.startingTime = "11:00";
        broadcastRepresentation.type = BroadcastEnum.NEWS;
        return broadcastRepresentation;
    }

    @Test
    public void suggestionsTest(){
        int size = broadcastRepository.listAll().size();
        BroadcastRepresentation br = createRepresentation();
        Broadcast broadcast = broadcastService.create(br);
        assertEquals(size + 1, broadcastRepository.listAll().size());
        String url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/" + broadcast.getId() + "/suggestions";
        // not authorized user
        given().when()
                .get(url)
                .then().statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
        // authorized user without permissions
        given().auth().preemptive().basic("user", "user")
                .when()
                .get(url)
                .then().statusCode(Response.Status.FORBIDDEN.getStatusCode());
        // authorized user with permissions valid broadcast
        SuggestionsDTO dto = given().auth().preemptive().basic("producer", "producer")
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(SuggestionsDTO.class);
        assertNotNull(dto);
        assertNotNull(dto.songs);
        assertNotNull(dto.ads);
        String invalidUrl = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/-1/suggestions";
        // authorized user with permissions invalid broadcast
        given().auth().preemptive().basic("producer", "producer")
                .when()
                .get(invalidUrl)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}
