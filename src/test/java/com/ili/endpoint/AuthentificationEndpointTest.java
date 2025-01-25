package com.ili.endpoint;

import com.ili.dto.Adulte;
import com.ili.dto.Role;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class AuthentificationEndpointTest {
    static Adulte adulte = new Adulte(null, "test", "test", "test1@mail.fr", Role.PARENT, "test", "0123456789", null, false);

    @BeforeAll
    public static void init() {
        given()
                .contentType("application/json")
                .body(adulte)
                .when().post("/adulte")
                .then()
                .statusCode(200);
    }

    @Test
    public void testLoginSuccess() {
        String loginPayload = "{\"mail\":\"test1@mail.fr\",\"motDePasse\":\"test\"}";

        Response rep = given()
                .contentType("application/json")
                .body(loginPayload)
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .extract().response();

        String responseBody = rep.getBody().asString();
        System.out.println("Token in response body: " + responseBody);

        assertTrue(responseBody.contains("token"));
    }

    @Test
    public void testLoginFailure() {
        String login = "{\"mail\":\"bad@bad.com\",\"motDePasse\":\"bad\"}";

        given()
                .contentType("application/json")
                .body(login)
                .when().post("/auth/login")
                .then()
                .statusCode(401);
    }
}

