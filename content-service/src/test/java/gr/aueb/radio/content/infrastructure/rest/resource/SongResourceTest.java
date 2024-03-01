package gr.aueb.radio.content.infrastructure.rest.resource;

import gr.aueb.radio.content.Fixture;
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
import gr.aueb.radio.content.infrastructure.service.user.representation.UserVerifiedRepresentation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.apache.maven.cli.internal.ExtensionResolutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        UserVerifiedRepresentation user = new UserVerifiedRepresentation();
        user.id = 1;
        user.role = "PRODUCER";
        Mockito.when(userService.verifyAuth(anyString())).thenReturn(user);

    }

    @Test
    public void testFindSong(){
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
        songInputDTO.title ="Test Song";
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
        songInputDTO.title ="Test Song";
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
    public void searchSongTest(){
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





//
//    @Test
//    public void deleteSongTest() {
//        // Create a new song to delete
//        SongRepresentation songRepresentation = createRepresentation();
//        Song newSong = songService.create(songRepresentation);
//        int originalNumOfBroadcasts = songRepository.listAll().size();
//
//        // Attempt to delete a song with an invalid ID
//        String invalidUrl = Fixture.API_ROOT + Fixture.SONGS_PATH + "/-1";
//        given().auth().preemptive().basic("producer", "producer")
//                .contentType(ContentType.JSON)
//                .when()
//                .delete(invalidUrl)
//                .then()
//                .statusCode(Status.NOT_FOUND.getStatusCode());
//
//        // Attempt to delete a song with a valid ID but no authenticated user
//        String url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/" + newSong.getId();
//        given()
//                .contentType(ContentType.JSON)
//                .when()
//                .delete(url)
//                .then()
//                .statusCode(Status.UNAUTHORIZED.getStatusCode());
//
//        // Attempt to delete a song with a valid ID but no delete permissions
//        given()
//                .auth().preemptive().basic("user", "user")
//                .contentType(ContentType.JSON)
//                .when()
//                .delete(url)
//                .then()
//                .statusCode(Status.FORBIDDEN.getStatusCode());
//
//        // Attempt to delete a song with a valid ID and delete permissions
//        given()
//                .auth().preemptive().basic("producer", "producer")
//                .contentType(ContentType.JSON)
//                .when()
//                .delete(url)
//                .then()
//                .statusCode(Status.NO_CONTENT.getStatusCode());
//
//        assertEquals(originalNumOfBroadcasts - 1, songRepository.listAll().size());
//    }
//
//
//    @Test
//    public void searchSongTest(){
//        String url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/";
//        List<SongRepresentation> songsFound = given()
//                .when()
//                .get(url)
//                .then()
//                .statusCode(Status.OK.getStatusCode())
//                .extract().as(new TypeRef<>() {
//                });
//        assertEquals(songRepository.listAll().size(), songsFound.size());
//    }
//
//    @Test
//    public void testUpdateSong() {
//        SongRepresentation songRepresentation = createRepresentation();
//        // Try to update immutable song
//        Song existingSong = songRepository.listAll().get(0);
//        String url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/" + existingSong.getId();
//        given()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(songRepresentation)
//                .when()
//                .put(url)
//                .then()
//                .statusCode(Status.BAD_REQUEST.getStatusCode());
//
//        // Try to update not existent song
//        url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/-1";
//        given()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(songRepresentation)
//                .when()
//                .put(url)
//                .then()
//                .statusCode(Status.NOT_FOUND.getStatusCode());
//
//        // create first a song to update it
//        Song song = songService.create(songRepresentation);
//        songRepresentation.year = songRepresentation.year - 10;
//
//        // Send PUT request to update song
//        url = Fixture.API_ROOT + Fixture.SONGS_PATH + "/" + song.getId();
//        given()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(songRepresentation)
//                .when()
//                .put(url)
//                .then()
//                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
//        Song updatedSong = songRepository.findById(song.getId());
//
//        // verify that the created song matches the expected values
//        assertEquals(songRepresentation.title, updatedSong.getTitle());
//        assertEquals(songRepresentation.artist, updatedSong.getArtist());
//        assertEquals(songRepresentation.genre, updatedSong.getGenre());
//        assertEquals(songRepresentation.duration, updatedSong.getDuration());
//        assertEquals(songRepresentation.year, updatedSong.getYear());
//
//    }

//    @Mock
//    private UriInfo mockUriInfo;
//    @Mock
//    private SongMapper mockSongMapper;
//    @Mock
//    private SongService mockSongService;
//
//    private SongResource songResourceUnderTest;
//
//    private AutoCloseable mockitoCloseable;
//
//    @BeforeEach
//    void setUp() {
//        mockitoCloseable = openMocks(this);
//        songResourceUnderTest = new SongResource();
//        songResourceUnderTest.uriInfo = mockUriInfo;
//        songResourceUnderTest.songMapper = mockSongMapper;
//        songResourceUnderTest.songService = mockSongService;
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        mockitoCloseable.close();
//    }
//
//    @Test
//    void testGetSong() {
//        // Setup
//        when(mockSongService.findSong(0, "auth")).thenReturn(new SongRepresentation());
//
//        // Run the test
//        final Response result = songResourceUnderTest.getSong(0, "auth");
//
//        // Verify the results
//    }
//
//    @Test
//    void testSearch() {
//        // Setup
//        when(mockSongService.search("artist", 0, "genreTitle", "title", List.of(0), "auth"))
//                .thenReturn(List.of(new SongRepresentation()));
//
//        // Run the test
//        final Response result = songResourceUnderTest.search("artist", 0, "genreTitle", "title", "1002", "auth");
//
//        // Verify the results
//    }
//
//    @Test
//    void testSearch_SongServiceReturnsNoItems() {
//        // Setup
//        when(mockSongService.search("artist", 0, "genreTitle", "title", List.of(0), "auth"))
//                .thenReturn(Collections.emptyList());
//
//        // Run the test
//        final Response result = songResourceUnderTest.search("artist", 0, "genreTitle", "title", "1002", "auth");
//
//        // Verify the results
//    }
//
//    @Test
//    void testCreate() {
//        // Setup
//        final SongInputDTO songRepresentation = new SongInputDTO();
//
//        // Configure SongService.create(...).
//        final Song song = new Song();
//        song.setTitle("title");
//        song.setArtist("artist");
//        song.setDuration(0);
//        final Genre genre = new Genre();
//        genre.setId(0);
//        song.setGenre(genre);
//        when(mockSongService.create(any(SongInputDTO.class), eq("auth"))).thenReturn(song);
//
//        when(mockSongMapper.toRepresentation(any(Song.class))).thenReturn(new SongRepresentation());
//
//        // Run the test
//        final Response result = songResourceUnderTest.create(songRepresentation, "auth");
//
//        // Verify the results
//    }
//
//    @Test
//    void testDelete() {
//        // Setup
//        // Run the test
//        final Response result = songResourceUnderTest.delete(0, "auth");
//
//        // Verify the results
//        verify(mockSongService).delete(0, "auth");
//    }
}
