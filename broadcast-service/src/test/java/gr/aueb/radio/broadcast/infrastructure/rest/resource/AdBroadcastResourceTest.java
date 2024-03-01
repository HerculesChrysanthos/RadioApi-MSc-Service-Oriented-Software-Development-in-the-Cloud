package gr.aueb.radio.broadcast.infrastructure.rest.resource;

import gr.aueb.radio.broadcast.application.BroadcastService;
import gr.aueb.radio.broadcast.application.ContentService;
import gr.aueb.radio.broadcast.application.UserService;
import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.ExternalServiceException;
import gr.aueb.radio.broadcast.common.IntegrationBase;
import gr.aueb.radio.broadcast.common.RadioException;
import gr.aueb.radio.broadcast.domain.adBroadcast.AdBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.AdBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.ApiPath;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdBroadcastCreationDTO;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.AdBroadcastRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.content.representation.AdBasicRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AdBroadcastResourceTest extends IntegrationBase {

    @Inject
    AdBroadcastRepository adBroadcastRepository;

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
    public void getAdBroadcastTest() {
        AdBroadcast validAd = adBroadcastRepository.listAll().get(0);
        String url =  ApiPath.Root.AD_BROADCASTS + "/" + validAd.getId();

        AdBroadcastRepresentation sb = given()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .when()
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(AdBroadcastRepresentation.class);

        assertEquals(validAd.getBroadcastDate(), DateUtil.setDate(sb.broadcastDate));
        assertEquals(validAd.getBroadcastTime(), DateUtil.setTime(sb.broadcastTime));
    }

    @Test
    public void testABNotFound() {
        String url = ApiPath.Root.AD_BROADCASTS + "/101010";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .get(url)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testSearch() {
        AdBroadcast validSb = adBroadcastRepository.listAll().get(0);
        String url = ApiPath.Root.AD_BROADCASTS + "/";

        List<AdBroadcastRepresentation> found = given()
                .when()
                .header("Authorization","auth")
                .queryParam("date", DateUtil.setDateToString(validSb.getBroadcastDate()))
                .get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<>(){});

        for (AdBroadcastRepresentation ab: found){
            assertEquals(DateUtil.setDate(ab.broadcastDate), validSb.getBroadcastDate());
        }
    }

    @Test
    public void testSearchNotProducer() {
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "USER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);
        String url = ApiPath.Root.AD_BROADCASTS + "/";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .get(url)
                .then().statusCode(403);
    }

    @Test
    public void testSearchExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.AD_BROADCASTS + "/";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .get(url)
                .then().statusCode(424);
    }

    @Test
    public void testABByIdExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.AD_BROADCASTS + "/10";
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .get(url)
                .then().statusCode(424);
    }

    @Test
    public void testABByIdRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth problem",403));
        String url = ApiPath.Root.AD_BROADCASTS + "/10";

        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .get(url)
                .then().statusCode(403);
    }

    @Test
    public void testCreateAdBroadcast() {
        AdBroadcastCreationDTO sb = new AdBroadcastCreationDTO();
        sb.broadcastId = 3002;
        sb.adId = 7597;
        sb.startingTime = "08:00";

        String url = ApiPath.Root.AD_BROADCASTS + "/";

        AdBasicRepresentation abr = new AdBasicRepresentation();
        abr.id = 1;
        when(contentService.getAd("auth",7597)).thenReturn(abr);

        AdBasicRepresentation abrContent = new AdBasicRepresentation();
        List<AdBasicRepresentation> list = new ArrayList<>();
        list.add(abrContent);

        when(contentService.getAdsByFilters("auth", null, "")).thenReturn(list);

        AdBroadcast abCreate = new AdBroadcast();
        abCreate.setId(101010);
        abCreate.setBroadcast(null);
        abCreate.setAd(7597);
        abCreate.setBroadcastTime(LocalTime.now());
        abCreate.setBroadcastDate(LocalDate.now());

        when(broadcastService.scheduleAd(sb.broadcastId, abr,  DateUtil.setTime(sb.startingTime), list )).thenReturn(abCreate);

        AdBroadcastRepresentation created = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(sb)
                .when()
                .post(url)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode()).extract().as(AdBroadcastRepresentation.class);

        assertNotNull(created);
    }

    @Test
    public void testCreateABRadioException() {
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 2;
        user.role = "USER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);

        AdBroadcastCreationDTO ab = new AdBroadcastCreationDTO();
        ab.broadcastId = 3002;
        ab.adId = 7597;
        ab.startingTime = "08:00";

        String url = ApiPath.Root.AD_BROADCASTS + "/";

        AdBasicRepresentation abr = new AdBasicRepresentation();
        abr.id = 1;
        when(contentService.getAd("auth",7597)).thenReturn(abr);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(ab)
                .when()
                .post(url)
                .then()
                .statusCode(403).extract();
    }

    @Test
    public void testCreateABNotFoundException() {
        AdBroadcastCreationDTO ab = new AdBroadcastCreationDTO();
        ab.broadcastId = 3002;
        ab.adId = 7597;
        ab.startingTime = "08:00";

        String url = ApiPath.Root.AD_BROADCASTS + "/";

        AdBasicRepresentation sbr = new AdBasicRepresentation();
        sbr.id = 1;
        when(contentService.getAd("auth",7597)).thenThrow(new NotFoundException("ad nf"));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(ab)
                .when()
                .post(url)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode()).extract();
    }

    @Test
    public void testCreateExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.AD_BROADCASTS ;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .post(url)
                .then().statusCode(424);
    }

    @Test
    public void testDeleteAdBroadcast() {
        String url = ApiPath.Root.AD_BROADCASTS + "/4001";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .delete(url)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).extract();
    }

    @Test
    public void testDeleteAdBroadcastNotFound() {
        String url = ApiPath.Root.AD_BROADCASTS + "/50010";
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
        String url = ApiPath.Root.AD_BROADCASTS + "/4001" ;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .delete(url)
                .then().statusCode(424);
    }

    @Test
    public void testDeleteRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("Problem on reaching user api."));
        String url = ApiPath.Root.AD_BROADCASTS + "/4001" ;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .delete(url)
                .then().statusCode(400);
    }

    @Test
    public void testDeleteWithFiltersExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        String url = ApiPath.Root.AD_BROADCASTS ;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .delete(url)
                .then().statusCode(424);
    }

    @Test
    public void testDeleteAdBroadcastWithFilters() {
        String url = ApiPath.Root.AD_BROADCASTS;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .queryParam("adId", 1001)
                .when()
                .delete(url)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).extract();
    }

    @Test
    public void testDeleteWithFiltersRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth problem",403));
        String url = ApiPath.Root.AD_BROADCASTS ;
        given()
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization","auth")
                .delete(url)
                .then().statusCode(403);
    }

}
