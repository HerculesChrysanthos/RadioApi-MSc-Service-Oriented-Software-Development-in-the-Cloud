package gr.aueb.radio.resources;

import gr.aueb.radio.Fixture;
import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.domains.Song;
import gr.aueb.radio.persistence.SongRepository;
import gr.aueb.radio.representations.SongRepresentation;
import gr.aueb.radio.services.SongService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class SongResourceTest extends IntegrationBase {

    @Inject
    SongRepository songRepository;

    @Inject
    SongService songService;

    public SongRepresentation createRepresentation() {
        SongRepresentation songRepresentation = new SongRepresentation();
        songRepresentation.title = "Test Song";
        songRepresentation.artist = "Test Artist";
        songRepresentation.genre = "Test Genre";
        songRepresentation.duration = 180;
        songRepresentation.year = 2021;
        return songRepresentation;
    }

    @Test
    public void testFindSong(){
        // find valid song in DB to search
        Song song = songRepository.listAll().get(0);
        // send get request to find the song
        String url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/" + song.getId();
        SongRepresentation foundSong = given()
                .contentType(ContentType.JSON)
                .when().get(url)
                .then().statusCode(Status.OK.getStatusCode())
                .extract().as(SongRepresentation.class);
        // verify that the found song matches the original song form db
        assertEquals(foundSong.title, song.getTitle());
        assertEquals(foundSong.artist, song.getArtist());
        assertEquals(foundSong.genre, song.getGenre());
        assertEquals(foundSong.duration, song.getDuration());
        assertEquals(foundSong.year, song.getYear());

        // try to find a song that does not exist
        url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/-1";
        given()
                .contentType(ContentType.JSON)
                .when().get(url)
                .then().statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testCreateSong() {
        int initNumOfSongs = songRepository.listAll().size();
        // create a new song representation
        SongRepresentation songRepresentation = createRepresentation();

        // send POST request to create the song
        String url = Fixture.API_ROOT + Fixture.SONGS_PATH;
        SongRepresentation createdSong = given()
                .contentType(ContentType.JSON)
                .body(songRepresentation)
                .when().post(url)
                .then().statusCode(Status.CREATED.getStatusCode())
                .extract().as(SongRepresentation.class);

        // verify that the song has been created
        assertEquals(initNumOfSongs + 1, songRepository.listAll().size());
        // verify that the created song matches the expected values
        assertEquals(songRepresentation.title, createdSong.title);
        assertEquals(songRepresentation.artist, createdSong.artist);
        assertEquals(songRepresentation.genre, createdSong.genre);
        assertEquals(songRepresentation.duration, createdSong.duration);
        assertEquals(songRepresentation.year, createdSong.year);
    }

    @Test
    public void deleteSongTest() {
        // Create a new song to delete
        SongRepresentation songRepresentation = createRepresentation();
        Song newSong = songService.create(songRepresentation);
        int originalNumOfBroadcasts = songRepository.listAll().size();

        // Attempt to delete a song with an invalid ID
        String invalidUrl = Fixture.API_ROOT + Fixture.SONGS_PATH + "/-1";
        given().auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .when()
                .delete(invalidUrl)
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());

        // Attempt to delete a song with a valid ID but no authenticated user
        String url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/" + newSong.getId();
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then()
                .statusCode(Status.UNAUTHORIZED.getStatusCode());

        // Attempt to delete a song with a valid ID but no delete permissions
        given()
                .auth().preemptive().basic("user", "user")
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then()
                .statusCode(Status.FORBIDDEN.getStatusCode());

        // Attempt to delete a song with a valid ID and delete permissions
        given()
                .auth().preemptive().basic("producer", "producer")
                .contentType(ContentType.JSON)
                .when()
                .delete(url)
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());

        assertEquals(originalNumOfBroadcasts - 1, songRepository.listAll().size());
    }


    @Test
    public void searchSongTest(){
        String url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/";
        List<SongRepresentation> songsFound = given()
                .when()
                .get(url)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract().as(new TypeRef<>() {
                });
        assertEquals(songRepository.listAll().size(), songsFound.size());
    }

    @Test
    public void testUpdateSong() {
        SongRepresentation songRepresentation = createRepresentation();
        // Try to update immutable song
        Song existingSong = songRepository.listAll().get(0);
        String url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/" + existingSong.getId();
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(songRepresentation)
                .when()
                .put(url)
                .then()
                .statusCode(Status.BAD_REQUEST.getStatusCode());

        // Try to update not existent song
        url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/-1";
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(songRepresentation)
                .when()
                .put(url)
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());

        // create first a song to update it
        Song song = songService.create(songRepresentation);
        songRepresentation.year = songRepresentation.year - 10;

        // Send PUT request to update song
        url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/" + song.getId();
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(songRepresentation)
                .when()
                .put(url)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        Song updatedSong = songRepository.findById(song.getId());

        // verify that the created song matches the expected values
        assertEquals(songRepresentation.title, updatedSong.getTitle());
        assertEquals(songRepresentation.artist, updatedSong.getArtist());
        assertEquals(songRepresentation.genre, updatedSong.getGenre());
        assertEquals(songRepresentation.duration, updatedSong.getDuration());
        assertEquals(songRepresentation.year, updatedSong.getYear());

    }
}



