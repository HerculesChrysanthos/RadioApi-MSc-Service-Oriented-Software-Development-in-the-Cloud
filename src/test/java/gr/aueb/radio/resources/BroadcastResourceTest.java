package gr.aueb.radio.resources;

import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.Fixture;
import gr.aueb.radio.domains.Broadcast;
import gr.aueb.radio.enums.BroadcastEnum;
import gr.aueb.radio.persistence.BroadcastRepository;
import gr.aueb.radio.persistence.SongBroadcastRepository;
import gr.aueb.radio.persistence.AdBroadcastRepository;
import gr.aueb.radio.representations.BroadcastOutputRepresentation;
import gr.aueb.radio.representations.BroadcastRepresentation;
import gr.aueb.radio.services.BroadcastService;
import gr.aueb.radio.utils.DateUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BroadcastResourceTest extends IntegrationBase {
    @Inject
    BroadcastRepository broadcastRepository;

    @Inject
    AdBroadcastRepository adBroadcastRepository;

    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    BroadcastService broadcastService;

    public BroadcastRepresentation createRepresentation(){
        BroadcastRepresentation broadcastRepresentation = new BroadcastRepresentation();
        broadcastRepresentation.startingTime = "12:00";
        broadcastRepresentation.startingDate = "12-12-2022";
        broadcastRepresentation.type = BroadcastEnum.NEWS;
        broadcastRepresentation.duration = 40;
        return broadcastRepresentation;
    }

    public BroadcastRepresentation updateRepresentation(String time, String date, BroadcastEnum type, Integer duration, BroadcastRepresentation br){
        br.startingTime = time;
        br.startingDate = date;
        br.type = type;
        br.duration = duration;
        return br;
    }

    @Test
    public void findBroadcastTest(){
        BroadcastRepresentation br = createRepresentation();
        // create a new broadcast that we will search
        Broadcast created = broadcastService.create(br);
        String url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/-1";
        // authorized user with permissions invalid broadcast
        given()
                .when()
                .get(url)
                .then().statusCode(Status.NOT_FOUND.getStatusCode());

        url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/" + created.getId();

        // authorized user with permissions valid broadcast
        BroadcastRepresentation found = given()
                .when()
                .get(url)
                .then().statusCode(Status.OK.getStatusCode())
                .extract().as(BroadcastRepresentation.class);

        assertEquals(br.type, created.getType());
        assertEquals(br.duration, created.getDuration());
        LocalTime brTime = DateUtil.setTime(found.startingTime);
        LocalDate brDate = DateUtil.setDate(found.startingDate);
        assertTrue(brTime.equals(created.getStartingTime()));
        assertTrue(brDate.equals(created.getStartingDate()));
    }

    @Test
    public void searchBroadcastTest(){
        String url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH;
        int totalNumOfBroadcasts = broadcastRepository.listAll().size();
        // general search should return all broadcasts
        List<BroadcastOutputRepresentation> found = given()
                .when()
                .get(url)
                .then().statusCode(Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });
        assertEquals(totalNumOfBroadcasts, found.size());
        // specific search should return only one broadcast
        Broadcast exists = broadcastRepository.listAll().get(0);
        found = given()
                .queryParam("from", DateUtil.setTimeToString(exists.getStartingTime()))
                .queryParam("to", DateUtil.setTimeToString(exists.getBroadcastEndingDateTime().toLocalTime()))
                .queryParam("date", DateUtil.setDateToString(exists.getStartingDate()))
                .queryParam("type", exists.getType())
                .when()
                .get(url)
                .then().statusCode(Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });
        assertEquals(1, found.size());
        BroadcastOutputRepresentation broadcastFound = found.get(0);
        assertEquals(exists.getSongBroadcasts().size(), broadcastFound.songBroadcasts.size());
        // TODO: Ad assertition for AdBroadcasts too
        assertEquals(exists.getDuration(), broadcastFound.duration);
        assertEquals(exists.getType(), broadcastFound.type);
        assertTrue(exists.getStartingDate().equals(DateUtil.setDate(broadcastFound.startingDate)));
        assertTrue(exists.getStartingTime().equals(DateUtil.setTime(broadcastFound.startingTime)));
    }

    @Test
    @TestTransaction
    public void createBroadcastTest(){
        BroadcastRepresentation br = createRepresentation();
        String url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH;
        int originalNumOfBroadcasts = broadcastRepository.listAll().size();
        // not authorized user
        given().contentType(ContentType.JSON)
                .body(br)
                .when()
                .post(url)
                .then().statusCode(Status.UNAUTHORIZED.getStatusCode());
        // authorized user without permissions
        given().auth().preemptive().basic("user", "user")
                .contentType(ContentType.JSON)
                .body(br)
                .when()
                .post(url)
                .then().statusCode(Status.FORBIDDEN.getStatusCode());
        // authorized user with permissions valid broadcast
        BroadcastOutputRepresentation broadcastRepresentation = given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .body(br)
                .when()
                .post(url)
                .then().statusCode(Status.CREATED.getStatusCode())
                .extract().as(BroadcastOutputRepresentation.class);
        assertEquals(br.type, broadcastRepresentation.type);
        assertEquals(br.duration, broadcastRepresentation.duration);
        assertTrue(br.startingTime.equals(broadcastRepresentation.startingTime));
        assertTrue(br.startingDate.equals(broadcastRepresentation.startingDate));
        assertEquals(originalNumOfBroadcasts + 1, broadcastRepository.listAll().size());
    }

    @Test
    public void triggerCreateBroadcastRestrictionTest(){
        // get an existing broadcast
        List<Broadcast> broadcasts = broadcastRepository.listAll();
        Broadcast existingBroadcast = broadcasts.get(0);
        //Take original number of broadcasts
        int originalNumOfBroadcasts = broadcasts.size();

        // Create a broadcast representation
        BroadcastRepresentation br = createRepresentation();
        // Update representation with invalid fields for the broadcast creation
        br.startingDate = DateUtil.setDateToString(existingBroadcast.getStartingDate());
        br.startingTime = DateUtil.setTimeToString(existingBroadcast.getStartingTime());

        String url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH;

        // authorized user with permissions invalid broadcast
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .body(br)
                .when()
                .post(url)
                .then().statusCode(Status.BAD_REQUEST.getStatusCode());

        // assert that no new broadcast has been created
        assertEquals(originalNumOfBroadcasts, broadcastRepository.listAll().size());
    }

    @Test
    public void updateBroadcastTest(){
        BroadcastRepresentation br = createRepresentation();
        // create a new broadcast that we will update
        Broadcast created = broadcastService.create(br);
        // provide updated values for broadcast
        BroadcastRepresentation updatedRepresentation = updateRepresentation("11:00", "22-08-1998", BroadcastEnum.PODCAST, 100, br);
        String url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/" + created.getId();

        // not authorized user
        given().contentType(ContentType.JSON)
                .body(br)
                .when()
                .put(url)
                .then().statusCode(Status.UNAUTHORIZED.getStatusCode());

        // authorized user without permissions
        given().auth().preemptive().basic("user", "user")
                .contentType(ContentType.JSON)
                .body(br)
                .when()
                .put(url)
                .then().statusCode(Status.FORBIDDEN.getStatusCode());
        // authorized user with permissions valid broadcast
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .body(br)
                .when()
                .put(url)
                .then().statusCode(Status.NO_CONTENT.getStatusCode());;

        Broadcast updated = broadcastRepository.findById(created.getId());

        assertEquals(updatedRepresentation.type, updated.getType());
        assertEquals(updatedRepresentation.duration, updated.getDuration());
        LocalTime brTime = DateUtil.setTime(updatedRepresentation.startingTime);
        LocalDate brDate = DateUtil.setDate(updatedRepresentation.startingDate);
        assertTrue(brTime.equals(updated.getStartingTime()));
        assertTrue(brDate.equals(updated.getStartingDate()));
    }

    @Test
    public void triggerUpdateBroadcastRestrictionTest(){
        // get an existing broadcast
        List<Broadcast> broadcasts = broadcastRepository.listAll();
        Broadcast existingBroadcast = broadcasts.get(0);

        // Create a broadcast representation
        BroadcastRepresentation br = createRepresentation();

        // trigger not found exception
        String url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/-1";
        // authorized user with permissions invalid broadcast
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .body(br)
                .when()
                .put(url)
                .then().statusCode(Status.NOT_FOUND.getStatusCode());


        url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/" + existingBroadcast.getId();
        // trigger radio exception
        // this can be done either if the broadcast is immutable or the broadcast update overlaps another broadcast
        // lets choose immutability
        // authorized user with permissions invalid broadcast
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .body(br)
                .when()
                .put(url)
                .then().statusCode(Status.BAD_REQUEST.getStatusCode());

        // Assert that no update has been made to broadcast
        Broadcast broadcastNotUpdated = broadcastRepository.findById(existingBroadcast.getId());
        assertEquals(broadcastNotUpdated.getStartingTime(), existingBroadcast.getStartingTime());
        assertEquals(broadcastNotUpdated.getStartingDate(), existingBroadcast.getStartingDate());
        assertEquals(broadcastNotUpdated.getType(), existingBroadcast.getType());
        assertEquals(broadcastNotUpdated.getDuration(), existingBroadcast.getDuration());
    }

    @Test
    public void deleteBroadcastTest(){
        List<Broadcast> initialBroadcasts = broadcastRepository.listAll();
        int initialNumOfBroadcasts = initialBroadcasts.size();
        int initialNumOfAdBroadcasts = adBroadcastRepository.listAll().size();
        int initialNumOfSongBroadcasts = songBroadcastRepository.listAll().size();
        Broadcast broadcastToDelete = initialBroadcasts.get(0);
        int scheduledAdsOfBroadcast = broadcastToDelete.getAdBroadcasts().size();
        int scheduledSongsOfBroadcast = broadcastToDelete.getSongBroadcasts().size();
        String url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/" + broadcastToDelete.getId();

        // not authorized user
        given().contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then().statusCode(Status.UNAUTHORIZED.getStatusCode());

        // authorized user without permissions
        given().auth().preemptive().basic("user", "user")
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then().statusCode(Status.FORBIDDEN.getStatusCode());

        // authorized user invalid broadcast to delete
        url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/-1";

        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then().statusCode(Status.NOT_FOUND.getStatusCode());

        // authorized user valid broadcast to delete
        url = Fixture.API_ROOT + Fixture.BROADCASTS_PATH + "/" + broadcastToDelete.getId();
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then().statusCode(Status.NO_CONTENT.getStatusCode());
        assertEquals(initialNumOfBroadcasts - 1, broadcastRepository.listAll().size());
        assertEquals(initialNumOfAdBroadcasts - scheduledAdsOfBroadcast, adBroadcastRepository.listAll().size());
        assertEquals(initialNumOfSongBroadcasts - scheduledSongsOfBroadcast, songBroadcastRepository.listAll().size());
    }
}
