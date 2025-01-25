package com.ili.endpoint;


import com.ili.dto.*;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class AdulteEndpointTest {

    @Test
    @Order(1)
    public void postAndGetAdulte() {
        Adulte adulte = new Adulte(null, "test", "test", "test@mail.fr", Role.PARENT, "test", "0123456789", null, false);
        Eleve eleve = new Eleve(null,"test","t","01/05/2010","ce1");
        adulte.setEleves(List.of(eleve));
        Eleve eleve2 = new Eleve(null,"testajout","t","01/05/2020","ce1");
        // POST
       given()
                .body(adulte)
                .header("Content-Type", "application/json")
                .post("/adulte")
                .then()
                .statusCode(200);

        // Get all
       Response response = given()
                .when().get("/adulte")
                .then()
                .statusCode(200)
               .extract().response();

       String responseBody = response.getBody().asString();

       String nom = JsonPath.from(responseBody).getString("[0].nom");
       String mail = JsonPath.from(responseBody).getString("[0].mail");

       //GET BY ID et test si bon user recupérer
       given()
                .pathParam("mail", mail)
                .when()
                .get("/adulte/{mail}")
                .then()
                .statusCode(200)
                .body("nom", equalTo(nom));
       adulte.setEleves(List.of(eleve,eleve2));
       //Update adulte
       given()
                .body(adulte)
                .header("Content-Type", "application/json")
                .put("/adulte")
                .then()
                .statusCode(200);
       Produit produit = new Produit(null,"produit","az",10,false);
       Produit produit2 = new Produit(null,"produit2","az",8,false);
       Evenement evenement = new Evenement(null,"a","05/05/2024",
                "05/06/2024","10/06/2024", List.of(),
                List.of(produit,produit2)
                ,true,true);

       given()
                .contentType(ContentType.JSON)
                .body(evenement)
                .when()
                .post("/evenement")
                .then()
                .statusCode(200);
       produit.setId(1L);
       produit2.setId(2L);
       adulte.setId(2L);
       CommandeProduit commandeProduit = new CommandeProduit(produit,4);
       CommandeProduit commandeProduit2 = new CommandeProduit(produit2,4);
       Commande commande=new Commande(null,false,false,1,adulte,null,List.of(commandeProduit,commandeProduit2));
       Commande commande2=new Commande(null,true,false,1,adulte,null,List.of(commandeProduit,commandeProduit2));
       given()
               .contentType(ContentType.JSON)
               .body(commande)
               .when()
               .post(("/commande"))
               .then()
               .statusCode(200);
       given()
                .contentType(ContentType.JSON)
                .body(commande2)
                .when()
                .post(("/commande"))
                .then()
                .statusCode(200);


    }
    @Test
    @Order(2)
    public void postAdm(){
        Adulte adulte = new Adulte(null, "test", "test", "adm@mail.fr", Role.ADMIN, "test", "0123456789", List.of(), false);
        given()
                .body(adulte)
                .header("Content-Type", "application/json")
                .post("/adulte")
                .then()
                .statusCode(200);
    }
}
