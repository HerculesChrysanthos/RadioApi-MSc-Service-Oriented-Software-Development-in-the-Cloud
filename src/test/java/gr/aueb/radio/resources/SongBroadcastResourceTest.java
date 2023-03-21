package gr.aueb.radio.resources;


import gr.aueb.radio.Fixture;
import gr.aueb.radio.IntegrationBaseExtended;
import gr.aueb.radio.domains.SongBroadcast;
import gr.aueb.radio.persistence.SongBroadcastRepository;
import gr.aueb.radio.representations.SongBroadcastRepresentation;
import gr.aueb.radio.services.SongBroadcastService;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class SongBroadcastResourceTest extends IntegrationBaseExtended  {


    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    SongBroadcastService SongBroadcastService;

    @Test
    @TestTransaction
    public void getSongBroadcastTest() {
        // Changed SongBroadcastService to songBroadcastService to match the injected field name
        SongBroadcast validsong = songBroadcastRepository.listAll().get(0);
        String url = Fixture.API_ROOT + Fixture.SONGBROADCAST_PATH + "/" + validsong.getId();

        SongBroadcastRepresentation sb = given().auth().preemptive().basic("producer", "producer")
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(SongBroadcastRepresentation.class);

        assertEquals(validsong.getBroadcastDate(), DateUtil.setDate(sb.broadcastDate));
        assertEquals(validsong.getBroadcastTime(), DateUtil.setTime(sb.broadcastTime));

        String url2 = Fixture.API_ROOT + Fixture.SONGBROADCAST_PATH + "/-1";
        given().auth().preemptive().basic("producer", "producer")
                .when()
                .get(url2)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteSongBroadcastTest() {
        List<SongBroadcast> initialSongBroadcast = songBroadcastRepository.listAll();
        int songBroadcastId = initialSongBroadcast.get(0).getId();
        int initialNumOfSongBroadcasts = initialSongBroadcast.size();

        String url = Fixture.API_ROOT + Fixture.SONGBROADCAST_PATH + "/-1";
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());

        // test successful delete
        String url1 = Fixture.API_ROOT + Fixture.SONGBROADCAST_PATH + "/" + songBroadcastId;
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .when()
                .delete(url1)
                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());

        int finalNumOfSongBroadcasts = songBroadcastRepository.listAll().size();
        // Changed finalNumOfAds to finalNumOfSongBroadcasts to match the variable name
        assertEquals(initialNumOfSongBroadcasts - 1, finalNumOfSongBroadcasts);
    }

}


