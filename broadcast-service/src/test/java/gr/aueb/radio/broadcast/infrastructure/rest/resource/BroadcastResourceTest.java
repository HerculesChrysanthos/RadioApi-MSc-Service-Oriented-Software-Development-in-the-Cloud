package gr.aueb.radio.broadcast.infrastructure.rest.resource;

import gr.aueb.radio.broadcast.application.BroadcastService;
import gr.aueb.radio.broadcast.application.ContentService;
import gr.aueb.radio.broadcast.application.UserService;
import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.IntegrationBase;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.broadcast.BroadcastType;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.BroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.ApiPath;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastOutputRepresentation;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.BroadcastRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.GenreBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class BroadcastResourceTest extends IntegrationBase {

    @Inject
    BroadcastRepository broadcastRepository;

//    @InjectMock
//    BroadcastService broadcastService;

    @InjectMock
    UserService userService;

    @InjectMock
    ContentService contentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        //RestAssured.defaultParser = Parser.JSON;
        user.id = 1;
        user.role = "PRODUCER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);
    }

    @Test
    public void getBroadcastTest() {
        Broadcast broadcast = broadcastRepository.listAll().get(0);
        String url = ApiPath.Root.BROADCASTS + "/" + broadcast.getId();

        BroadcastOutputRepresentation bor = given()
                .header("Authorization", "auth")
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(BroadcastOutputRepresentation.class);

        assertEquals(broadcast.getGenreId(), bor.genreId);
    }

    @Test
    public void testBroadcastNotFound() {
        String url = ApiPath.Root.BROADCASTS + "/101010";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testFindBroadcastByIdExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.BROADCASTS + "/1010";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(424);
    }

    @Test
    public void testSearchBroadcastsByDate() {
        Broadcast broadcast = broadcastRepository.listAll().get(0);

        String url = ApiPath.Root.BROADCASTS;
        List<BroadcastOutputRepresentation> found = given()
                .when()
                .header("Authorization", "auth")
                .queryParam("date", DateUtil.setDateToString(broadcast.getStartingDate()))
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });

        for (BroadcastOutputRepresentation s : found) {
            assertEquals(s.id, broadcast.getId());
        }
    }

    @Test
    public void testSearchBroadcastsByType() {
        Broadcast broadcast = broadcastRepository.listAll().get(0);

        String url = ApiPath.Root.BROADCASTS;
        List<BroadcastOutputRepresentation> found = given()
                .when()
                .header("Authorization", "auth")
                .queryParam("typeParam", broadcast.getType())
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });

        //for (BroadcastOutputRepresentation s: found) {
        assertEquals(found.size(), 2);
        //}
    }

    @Test
    public void testSearchBroadcastsByWrongType() {
        String url = ApiPath.Root.BROADCASTS;
        given()
                .when()
                .header("Authorization", "auth")
                .queryParam("type", "some type")
                .get(url)
                .then().statusCode(422);
    }

    @Test
    public void testGetNow() {
        String url = ApiPath.Root.BROADCASTS + "/now";
        given()
                .when()
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(200);
    }

    @Test
    public void testGetNowExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.BROADCASTS + "/now";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(424);
    }

    @Test
    public void testGetNowNotValidUser() {
        Mockito.when(userService.verifyAuth(anyString())).thenThrow(new RadioException("auth err", 401));
        String url = ApiPath.Root.BROADCASTS + "/now";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(401);
    }

    @Test
    public void testCreate() {
        String url = ApiPath.Root.BROADCASTS;

        BroadcastRepresentation br = new BroadcastRepresentation();
        br.duration = 100;
        br.genreId = 1;
        br.startingDate = "10-01-2022";
        br.startingTime = "00:00";
        br.type = BroadcastType.PODCAST;

        BroadcastOutputRepresentation created = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(br)
                .when()
                .post(url)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract()
                .as(BroadcastOutputRepresentation.class);

        assertNotNull(created);
        assertEquals(100, created.duration);
        assertEquals(1, created.userId);
    }

    @Test
    public void testCreateExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.BROADCASTS;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .post(url)
                .then().statusCode(424);
    }

    @Test
    public void testCreateNotValidUser() {
        Mockito.when(userService.verifyAuth(anyString())).thenThrow(new RadioException("auth err", 401));
        String url = ApiPath.Root.BROADCASTS;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .post(url)
                .then().statusCode(401);
    }

    @Test
    public void testUpdate() {
        Broadcast broadcast = broadcastRepository.listAll().get(1);

        BroadcastRepresentation br = new BroadcastRepresentation();
        br.genreId = 2;
        br.duration = broadcast.getDuration();
        br.type = broadcast.getType();
        br.startingTime = String.valueOf(broadcast.getStartingTime());
        br.startingDate = "01-01-2022";
        String url = ApiPath.Root.BROADCASTS + "/" + broadcast.getId();


        BroadcastService mockBrService = Mockito.mock(BroadcastService.class);


        when(mockBrService.checkForOverlapping(any(LocalDate.class), any(LocalTime.class), any(LocalTime.class), anyInt()))
                .thenReturn(false);

        GenreBasicRepresentation gbr = new GenreBasicRepresentation();
        gbr.id = 2;
        when(contentService.getGenreById(2)).thenReturn(gbr);
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(br)
                .when()
                .put(url)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testUpdateExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));

        Broadcast broadcast = broadcastRepository.listAll().get(0);

        String url = ApiPath.Root.BROADCASTS + "/" + broadcast.getId();

        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .put(url)
                .then().statusCode(424);
    }

    @Test
    public void testUpdateNotValidUser() {
        Mockito.when(userService.verifyAuth(anyString())).thenThrow(new RadioException("auth err", 401));
        Broadcast broadcast = broadcastRepository.listAll().get(0);

        String url = ApiPath.Root.BROADCASTS + "/" + broadcast.getId();
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .put(url)
                .then().statusCode(401);
    }

    @Test
    public void testUpdateBrNotFound() {
        Broadcast broadcast = broadcastRepository.listAll().get(1);

        BroadcastRepresentation br = new BroadcastRepresentation();
        br.genreId = 2;
        br.duration = broadcast.getDuration();
        br.type = broadcast.getType();
        br.startingTime = String.valueOf(broadcast.getStartingTime());
        br.startingDate = "01-01-2022";
        String url = ApiPath.Root.BROADCASTS + "/" + 101010;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(br)
                .when()
                .put(url)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testDelete() {
        Broadcast broadcast = broadcastRepository.listAll().get(1);

        String url = ApiPath.Root.BROADCASTS + "/" + broadcast.getId();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .delete(url)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testDeleteNotFound() {
        String url = ApiPath.Root.BROADCASTS + "/" + 101010;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .delete(url)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testDeleteExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));

        Broadcast broadcast = broadcastRepository.listAll().get(0);

        String url = ApiPath.Root.BROADCASTS + "/" + broadcast.getId();

        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .delete(url)
                .then().statusCode(424);
    }

    @Test
    public void testDeleteNotValidUser() {
        Mockito.when(userService.verifyAuth(anyString())).thenThrow(new RadioException("auth err", 401));
        Broadcast broadcast = broadcastRepository.listAll().get(0);

        String url = ApiPath.Root.BROADCASTS + "/" + broadcast.getId();
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .delete(url)
                .then().statusCode(401);
    }

    @Test
    public void testGetSuggestions() {
        Broadcast broadcast = broadcastRepository.listAll().get(1);

        String url = ApiPath.Root.BROADCASTS + "/" + broadcast.getId() + "/suggestions";
        given()
                .when()
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(200);
        //broadcast not found
        String url1 = ApiPath.Root.BROADCASTS + "/" + 111010 + "/suggestions";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .get(url1)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
        // test external exception
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(424);


        //test for invalid user
        Mockito.when(userService.verifyAuth(anyString())).thenThrow(new RadioException("auth err", 401));
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(401);



    }



}
