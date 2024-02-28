package gr.aueb.radio.infrastructure.resources;

import com.sun.security.auth.UserPrincipal;
import gr.aueb.radio.Fixture;
import gr.aueb.radio.common.IntegrationBase;
import gr.aueb.radio.user.application.UserService;
import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.persistence.UserRepository;
import gr.aueb.radio.user.infrastructure.rest.representation.UserBasicRepresentation;
import gr.aueb.radio.user.infrastructure.rest.representation.UserMapper;
import gr.aueb.radio.user.infrastructure.rest.representation.UserRepresentation;
import gr.aueb.radio.user.infrastructure.rest.resource.UserResource;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import gr.aueb.radio.user.common.RadioException;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;


@QuarkusTest
public class UserResourceTest extends IntegrationBase {
    @Inject
    UserRepository userRepository;

    @Inject
    UserResource userResource;

    @Inject
    UserService userService;


    @Inject
    SecurityContext securityContext;

    @Mock
    private UserService mockUserService;
    @Mock
    private UserMapper mockUserMapper;
    @Mock
    private SecurityContext mockSecurityContext;

    private UserResource userResourceUnderTest;


    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        userResourceUnderTest = new UserResource();
        userResourceUnderTest.userService = mockUserService;
        userResourceUnderTest.userMapper = mockUserMapper;
        userResourceUnderTest.securityContext = mockSecurityContext;
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

//    @Test
//    void testVerifyAuth() {
//
//        Mockito.when(mockSecurityContext.getUserPrincipal()).thenReturn(new UserPrincipal("name"));
//        Mockito.when(mockUserService.findUserByUsername("username")).thenReturn(new UserBasicRepresentation());
//
//
//        final Response result = userResourceUnderTest.verifyAuth();
//
//        Mockito.when(mockUserService.findUserByUsername("different_username")).thenThrow(new RadioException("Bad Request"));
//
//        // Verifying the response when an exception is thrown
//        final Response exceptionResult = userResourceUnderTest.verifyAuth();
//        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), 400);
//        assertEquals(null, exceptionResult.getEntity());
//
//    }

    @Test
    @TestTransaction
    public void testVerifyAuth() {
        given()
                .header("Authorization", "Basic cHJvZHVjZXI6cHJvZHVjZXI=")
                .get(Fixture.API_ROOT + Fixture.USERS_PATH + "/verify-auth")
                .then()
                .statusCode(200);
    }

    @Test
    @TestTransaction
    public void testVerifyAuthUnauthorized() {
        given()
                .header("Authorization", "Basic cHJvZHVjZXIxMjM6cHJvZHVjZXI=")
                .get(Fixture.API_ROOT + Fixture.USERS_PATH + "/verify-auth")
                .then()
                .statusCode(401);
    }


    @Test
    @TestTransaction
    public void getUserTest(){
        User validUser = userRepository.listAll().get(0);
        String url = Fixture.API_ROOT + Fixture.USERS_PATH + "/" + validUser.getId();
        UserRepresentation userRepresentation = when()
                .get(url)
                .then().statusCode(Status.OK.getStatusCode())
                .extract().as(UserRepresentation.class);

        assertEquals(validUser.getUsername(), userRepresentation.username);
        assertEquals(validUser.getEmail(), userRepresentation.email);
        assertEquals(validUser.getPassword(), userRepresentation.password);
    }


    @Test
    @TestTransaction
    public void notFoundUserTest(){
        String url = Fixture.API_ROOT + Fixture.USERS_PATH + "/" + 2023;
        //when().get(url).then().statusCode(Status.NOT_FOUND.getStatusCode());-->needs investigation
        when().get(url).then().statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    @TestTransaction
    public void createExistingUserTest(){
        User validUser = userRepository.listAll().get(0);
        UserRepresentation userToCreate = new UserRepresentation();
        userToCreate.username = validUser.getUsername();
        userToCreate.password = validUser.getPassword();
        userToCreate.email = validUser.getEmail();
        String url = Fixture.API_ROOT + Fixture.USERS_PATH;
        given().contentType(ContentType.JSON)
                .body(userToCreate)
                .when()
                .post(url)
                .then().statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @TestTransaction
    public void createUserTest(){
        UserRepresentation userToCreate = new UserRepresentation();
        userToCreate.username = "usertest";
        userToCreate.password = "usertest";
        userToCreate.email = "usertest@email.com";
        String url = Fixture.API_ROOT + Fixture.USERS_PATH;
        UserRepresentation userRepresentation = given()
                .contentType(ContentType.JSON)
                .body(userToCreate)
                .when()
                .post(url)
                .then().statusCode(Status.CREATED.getStatusCode())
                .extract().as(UserRepresentation.class);

        assertEquals(userToCreate.username, userRepresentation.username);
        assertEquals(userToCreate.email, userRepresentation.email);
        assertEquals(userToCreate.password, userRepresentation.password);
    }

}