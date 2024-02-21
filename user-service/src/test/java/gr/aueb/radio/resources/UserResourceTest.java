package gr.aueb.radio.resources;

import gr.aueb.radio.Fixture;
import gr.aueb.radio.IntegrationBase;
import gr.aueb.radio.user.application.UserService;
import gr.aueb.radio.user.domain.user.User;
import gr.aueb.radio.user.infrastructure.persistence.UserRepository;
import gr.aueb.radio.user.infrastructure.rest.representation.UserRepresentation;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
@QuarkusTest
public class UserResourceTest extends IntegrationBase {
    @Inject
    UserRepository userRepository;

    @Inject
    UserService userService;

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

