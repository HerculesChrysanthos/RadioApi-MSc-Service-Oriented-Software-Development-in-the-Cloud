package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.Fixture;
import gr.aueb.radio.content.application.BroadcastService;
import gr.aueb.radio.content.application.SongService;
import gr.aueb.radio.content.application.UserService;
import gr.aueb.radio.content.common.ExternalServiceException;
import gr.aueb.radio.content.common.IntegrationBase;
import gr.aueb.radio.content.common.RadioException;
import gr.aueb.radio.content.domain.genre.Genre;
import gr.aueb.radio.content.domain.song.Song;
import gr.aueb.radio.content.infrastructure.persistence.SongRepository;
import gr.aueb.radio.content.infrastructure.rest.representation.GenreRepresentation;
import gr.aueb.radio.content.infrastructure.rest.representation.SongInputDTO;
import gr.aueb.radio.content.infrastructure.rest.representation.SongMapper;
import gr.aueb.radio.content.infrastructure.rest.representation.SongRepresentation;
import gr.aueb.radio.content.infrastructure.service.broadcast.representation.SongBroadcastBasicRepresentation;
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.apache.maven.cli.internal.ExtensionResolutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@QuarkusTest
class SongResourceTest extends IntegrationBase {

    @Inject
    SongRepository songRepository;

    @InjectMock
    UserService userService;

    @InjectMock
    BroadcastService broadcastService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "PRODUCER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);

    }

    @Test
    public void testFindSong() {
        // find valid song in DB to search
        Song song = songRepository.listAll().get(0);
        // send get request to find the song
        String url = Fixture.API_ROOT + Fixture.SONGS + "/" + song.getId();
        SongRepresentation foundSong = given()
                .contentType(ContentType.JSON)
                .when().get(url)
                .then().statusCode(Response.Status.OK.getStatusCode())
                .extract().as(SongRepresentation.class);
        // verify that the found song matches the original song form db
        assertEquals(foundSong.title, song.getTitle());
        assertEquals(foundSong.artist, song.getArtist());
        assertEquals(foundSong.genre.id, song.getGenre().getId());
        assertEquals(foundSong.duration, song.getDuration());
        assertEquals(foundSong.year, song.getYear());

        // try to find a song that does not exist
        url = Fixture.API_ROOT + Fixture.SONGS + "/-1";
        given()
                .contentType(ContentType.JSON)
                .when().get(url)
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }


    @Test
    public void tesGetSongExternalServiceException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));

        String url = "/api/songs/1";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().get(url)
                .then().statusCode(424);
    }

    @Test
    public void tesGetSongRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth error", 403));

        String url = "/api/songs/1";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().get(url)
                .then().statusCode(403);
    }

    @Test
    void testCreateSong() {
        SongInputDTO songInputDTO = new SongInputDTO();
        GenreRepresentation genreRepresentation = new GenreRepresentation();
        genreRepresentation.id = 1;
        genreRepresentation.title = "genre-title";
        songInputDTO.title = "Test Song";
        songInputDTO.artist = "Test Artist";
        songInputDTO.genreId = 1;
        songInputDTO.duration = 180;
        songInputDTO.year = 2021;

        Song song = new Song();
        song.setId(1);
        song.setTitle("Test Song");
        song.setArtist("Test Artist");
        song.setDuration(180);
        song.setYear(2021);

        String url = "/api/songs";
        SongRepresentation createdSong = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(songInputDTO)
                .when().post(url)
                .then().statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(SongRepresentation.class);

        assertNotNull(createdSong);
        assertEquals(song.getTitle(), createdSong.title);
        assertEquals(song.getArtist(), createdSong.artist);
        assertEquals(song.getDuration(), createdSong.duration);
        assertEquals(song.getYear(), createdSong.year);
    }

    @Test
    public void testCreateBadRequest() {
        doThrow(new RadioException("Invalid song input")).when(userService).verifyAuth(any(String.class));
        SongInputDTO songInputDTO = new SongInputDTO();

        String url = "/api/songs";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(songInputDTO)
                .when().post(url)
                .then().statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void tesCreateRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth error", 403));

        String url = "/api/songs";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().post(url)
                .then().statusCode(403);
    }

    @Test
    public void testCreateExternalServiceException() {

        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));
        SongInputDTO songInputDTO = new SongInputDTO();
        GenreRepresentation genreRepresentation = new GenreRepresentation();
        genreRepresentation.id = 1;
        genreRepresentation.title = "genre-title";
        songInputDTO.title = "Test Song";
        songInputDTO.artist = "Test Artist";
        songInputDTO.genreId = 1;
        songInputDTO.duration = 180;
        songInputDTO.year = 2021;

        String url = "/api/songs";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(songInputDTO)
                .when().post(url)
                .then().statusCode(424);
    }


    @Test
    public void searchSongTest() {
        String url = Fixture.API_ROOT + Fixture.SONGS + "/";
        List<SongRepresentation> songsFound = given()
                .when()
                .get(url)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });
        assertEquals(songRepository.listAll().size(), songsFound.size());
    }

    @Test
    public void testSearchSongsWithFilters() {
        String url = Fixture.API_ROOT + Fixture.SONGS + "/";

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .queryParam("songsIds", "7001")
                .when()
                .get(url)
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).extract();
    }

    @Test
    public void tesSearchWithFiltersRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth error", 403));

        String url = Fixture.API_ROOT + Fixture.SONGS + "/";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().get(url)
                .then().statusCode(403);
    }

    @Test
    public void tesSearchWithFiltersExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));

        String url = Fixture.API_ROOT + Fixture.SONGS + "/";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when().get(url)
                .then().statusCode(424);
    }

    @Test
    public void testDelete() {
        String url = Fixture.API_ROOT + Fixture.SONGS + "/7001";

        List<SongBroadcastBasicRepresentation> s = new ArrayList<>();
        when(broadcastService.getSongBroadcastsBySongId("auth", 7001)).thenReturn(s);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .delete(url)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()).extract();
    }

    @Test
    public void testDeleteNotFound() {
        String url = Fixture.API_ROOT + Fixture.SONGS + "/71001";

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .delete(url)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode()).extract();
    }

    @Test
    public void testDeleteRadioException() {
        String url = Fixture.API_ROOT + Fixture.SONGS + "/7001";
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth error", 403));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .delete(url)
                .then()
                .statusCode(403).extract();
    }

    @Test
    public void testDeleteExternalException() {
        String url = Fixture.API_ROOT + Fixture.SONGS + "/7001";
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .when()
                .delete(url)
                .then()
                .statusCode(424).extract();
    }

//    @Test
//    public void testUpdate() {
//
//    }

    @Test
    public void testUpdateSong() {
        SongInputDTO songInputDTO = new SongInputDTO();

        GenreRepresentation genreRepresentation = new GenreRepresentation();
        genreRepresentation.id = 1;
        genreRepresentation.title = "genre-title";
        songInputDTO.title = "Test Song";
        songInputDTO.artist = "Test Artist";
        songInputDTO.genreId = 1;
        songInputDTO.duration = 180;
        songInputDTO.year = 2021;

        Song existingSong = songRepository.listAll().get(0);
        String url = Fixture.API_ROOT + Fixture.SONGS + "/" + existingSong.getId();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(songInputDTO)
                .when()
                .put(url)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testUpdateNotFoundException() {
        SongInputDTO songInputDTO = new SongInputDTO();

        GenreRepresentation genreRepresentation = new GenreRepresentation();
        genreRepresentation.id = 1;
        genreRepresentation.title = "genre-title";
        songInputDTO.title = "Test Song";
        songInputDTO.artist = "Test Artist";
        songInputDTO.genreId = 1;
        songInputDTO.duration = 180;
        songInputDTO.year = 2021;

        String url = Fixture.API_ROOT + Fixture.SONGS + "/10101010";
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(songInputDTO)
                .when()
                .put(url)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testUpdateExternalException() {
        when(userService.verifyAuth("auth")).thenThrow(new ExternalServiceException("Problem on reaching user api."));

        SongInputDTO songInputDTO = new SongInputDTO();

        GenreRepresentation genreRepresentation = new GenreRepresentation();
        genreRepresentation.id = 1;
        genreRepresentation.title = "genre-title";
        songInputDTO.title = "Test Song";
        songInputDTO.artist = "Test Artist";
        songInputDTO.genreId = 1;
        songInputDTO.duration = 180;
        songInputDTO.year = 2021;

        Song existingSong = songRepository.listAll().get(0);
        String url = Fixture.API_ROOT + Fixture.SONGS + "/" + existingSong.getId();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(songInputDTO)
                .when()
                .put(url)
                .then()
                .statusCode(424);
    }

    @Test
    public void testUpdateRadioException() {
        when(userService.verifyAuth("auth")).thenThrow(new RadioException("auth error", 403));

        SongInputDTO songInputDTO = new SongInputDTO();

        GenreRepresentation genreRepresentation = new GenreRepresentation();
        genreRepresentation.id = 1;
        genreRepresentation.title = "genre-title";
        songInputDTO.title = "Test Song";
        songInputDTO.artist = "Test Artist";
        songInputDTO.genreId = 1;
        songInputDTO.duration = 180;
        songInputDTO.year = 2021;

        Song existingSong = songRepository.listAll().get(0);
        String url = Fixture.API_ROOT + Fixture.SONGS + "/" + existingSong.getId();
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "auth")
                .body(songInputDTO)
                .when()
                .put(url)
                .then()
                .statusCode(403);
    }

}