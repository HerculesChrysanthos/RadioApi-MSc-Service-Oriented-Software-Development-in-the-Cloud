package gr.aueb.radio.broadcast.infrastructure.rest.resource;

import gr.aueb.radio.broadcast.application.BroadcastService;
import gr.aueb.radio.broadcast.application.ContentService;
import gr.aueb.radio.broadcast.application.SongBroadcastService;
import gr.aueb.radio.broadcast.application.UserService;
import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.IntegrationBase;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.broadcast.Broadcast;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.SongBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.ApiPath;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.SongBroadcastCreationDTO;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.SongBroadcastRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.SongBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class SongBroadcastResourceTest extends IntegrationBase {


    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @InjectMock
    BroadcastService broadcastService;

    @InjectMock
    UserService userService;

    @InjectMock
    ContentService contentService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "PRODUCER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);
    }

    @Test
    @TestTransaction
    public void getSongBroadcastTest() {
        SongBroadcast validsong = songBroadcastRepository.listAll().get(0);
        String url =  ApiPath.Root.SONG_BROADCASTS + "/" + validsong.getId();

        SongBroadcastRepresentation sb = given()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(SongBroadcastRepresentation.class);

        assertEquals(validsong.getBroadcastDate(), DateUtil.setDate(sb.broadcastDate));
        assertEquals(validsong.getBroadcastTime(), DateUtil.setTime(sb.broadcastTime));
    }

    @Test
    public void testSBNotFound() {
        String url = ApiPath.Root.SONG_BROADCASTS + "/101010";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .get(url)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testSearch() {
        SongBroadcast validSb = songBroadcastRepository.listAll().get(0);
        String url = ApiPath.Root.SONG_BROADCASTS + "/";

        List<SongBroadcastRepresentation> found = given()
                .when()
                .header("Authorization","auth")
                .queryParam("date", DateUtil.setDateToString(validSb.getBroadcastDate()))
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<>(){});

        for (SongBroadcastRepresentation sb: found){
            assertEquals(DateUtil.setDate(sb.broadcastDate), validSb.getBroadcastDate());
        }
    }

    @Test
    public void testSearchNotProducer() {
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "USER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);
        String url = ApiPath.Root.SONG_BROADCASTS + "/";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .get(url)
                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testSearchExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.SONG_BROADCASTS + "/";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(424);
    }

    @Test
    public void testSBByIdExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.SONG_BROADCASTS + "/10";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .get(url)
                .then().statusCode(424);
    }

    @Test
    public void testCreateSongBroadcast() {
        List<SongBroadcast> songBroadcastListBroadcastsList = songBroadcastRepository.listAll();
        int numOfSongBroadcasts = songBroadcastListBroadcastsList.size();
        SongBroadcastCreationDTO sb = new SongBroadcastCreationDTO();
        sb.broadcastId = 3002;
        sb.songId = 7597;
        sb.startingTime = "08:00";

        String url = ApiPath.Root.SONG_BROADCASTS + "/";

        SongBroadcast createdBroadcast = new SongBroadcast();

        SongBasicRepresentation sbr = new SongBasicRepresentation();
        sbr.id = 1;
        when(contentService.getSong("auth",7597)).thenReturn(sbr);

        SongBasicRepresentation sbrContent = new SongBasicRepresentation();
        List<SongBasicRepresentation> list = new ArrayList<>();
        list.add(sbrContent);

        when(contentService.getSongsByFilters("auth", null, null,null, null,"")).thenReturn(list);

        SongBroadcast sbcreate = new SongBroadcast();
        sbcreate.setId(101010);
        sbcreate.setBroadcast(null);
        sbcreate.setSong(7597);
        sbcreate.setBroadcastTime(LocalTime.now());
        sbcreate.setBroadcastDate(LocalDate.now());

        when(broadcastService.scheduleSong(sb.broadcastId, sbr,  DateUtil.setTime(sb.startingTime), list )).thenReturn(sbcreate);

        SongBroadcastRepresentation created = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(sb)
                .when()
                .post(url)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode()).extract().as(SongBroadcastRepresentation.class);

        assertNotNull(created);
    }

    @Test
    public void testCreateSBRadioException() {
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 2;
        user.role = "USER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);

        SongBroadcastCreationDTO sb = new SongBroadcastCreationDTO();
        sb.broadcastId = 3002;
        sb.songId = 7597;
        sb.startingTime = "08:00";

        String url = ApiPath.Root.SONG_BROADCASTS + "/";

        SongBasicRepresentation sbr = new SongBasicRepresentation();
        sbr.id = 1;
        when(contentService.getSong("auth",7597)).thenReturn(sbr);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(sb)
                .when()
                .post(url)
                .then()
                .statusCode(403).extract();
    }

    @Test
    public void testCreateSBNotFoundException() {
        SongBroadcastCreationDTO sb = new SongBroadcastCreationDTO();
        sb.broadcastId = 3002;
        sb.songId = 7597;
        sb.startingTime = "08:00";

        String url = ApiPath.Root.SONG_BROADCASTS + "/";

        SongBasicRepresentation sbr = new SongBasicRepresentation();
        sbr.id = 1;
        //when(contentService.getSong("auth",7597)).thenThrow(new NotFoundException("song nf"));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(sb)
                .when()
                .post(url)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode()).extract();
    }

    @Test
    public void testCreateExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.SONG_BROADCASTS ;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .post(url)
                .then().statusCode(424);
    }

    @Test
    public void testDeleteSongBroadcast() {
        String url = ApiPath.Root.SONG_BROADCASTS + "/5001";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .delete(url)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).extract();
    }

    @Test
    public void testDeleteSongBroadcastNotFound() {
        String url = ApiPath.Root.SONG_BROADCASTS + "/50010";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .delete(url)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode()).extract();
    }

    @Test
    public void testDeleteExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.SONG_BROADCASTS + "/5001" ;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .delete(url)
                .then().statusCode(424);
    }

    @Test
    public void testDeleteWithFiltersExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.SONG_BROADCASTS ;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .delete(url)
                .then().statusCode(424);
    }

    @Test
    public void testDeleteSongBroadcastWithFilters() {
        String url = ApiPath.Root.SONG_BROADCASTS;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .queryParam("songId", 7001)
                .when()
                .delete(url)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).extract();
    }

    @Test
    public void testDeleteWithFiltersRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth problem"));
        String url = ApiPath.Root.SONG_BROADCASTS ;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .delete(url)
                .then().statusCode(400);
    }
}
