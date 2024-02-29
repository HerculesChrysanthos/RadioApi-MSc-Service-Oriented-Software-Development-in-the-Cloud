package gr.aueb.radio.broadcast.infrastucture.rest.resource;

import gr.aueb.radio.broadcast.application.SongBroadcastService;
import gr.aueb.radio.broadcast.application.UserService;
import gr.aueb.radio.broadcast.common.DateUtil;
import gr.aueb.radio.broadcast.common.IntegrationBase;
import gr.aueb.radio.broadcast.domain.songBroadcast.SongBroadcast;
import gr.aueb.radio.broadcast.infrastructure.persistence.SongBroadcastRepository;
import gr.aueb.radio.broadcast.infrastructure.rest.ApiPath;
import gr.aueb.radio.broadcast.infrastructure.rest.representation.SongBroadcastRepresentation;
import gr.aueb.radio.broadcast.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@QuarkusTest
public class SongBroadcastResourceTest extends IntegrationBase {


    @Inject
    SongBroadcastRepository songBroadcastRepository;

    @Inject
    SongBroadcastService SongBroadcastService;

    @InjectMock
    UserService userService;

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


}
