package com.ili.endpoint;

import com.ili.dto.Eleve;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class EleveEndpointTest {

    @Test
    public void postAndGetEleve() {
        Eleve eleve = new Eleve(null, "prenomTest", "nomTest", "01/01/2015","cm1");

        // POST
        given()
                .body(eleve)
                .header("Content-Type", "application/json")
                .post("/eleve")
                .then()
                .statusCode(200);

        // Get all
        Response response = given()
                .when().get("/eleve")
                .then()
                .statusCode(200)
                .extract().response();

        String responseBody = response.getBody().asString();


        if (!responseBody.isEmpty()) {
            int eleveId = JsonPath.from(responseBody).getInt("[0].id");
            String nom = JsonPath.from(responseBody).getString("[0].nom");


            given()
                    .pathParam("id", eleveId)
                    .when()
                    .get("/eleve/{id}")
                    .then()
                    .statusCode(200)
                    .body("nom", equalTo(nom));
        }
    }
}
