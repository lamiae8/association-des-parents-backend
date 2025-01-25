package com.ili.controller;

import com.ili.dto.Evenement;
import com.ili.dto.Produit;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
class EvenementControllerTest {

//    @Test
//    void testCreateEvenementSuccess() {
//
//        Evenement evenement = new Evenement(null,"a","05/05/2024",
//                "05/06/2024","10/06/2024", List.of(),
//                List.of(new Produit(null,"produit","az",10,false))
//                ,true,true,0);
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(evenement)
//                .when()
//                .post("/evenement")
//                .then()
//                .statusCode(200);
//    }


//    @Test
//    void testCreateEvenementFailure() {
//        String evenementJson = """
//        {
//            "nom": "Event Name",
//            "dateDebut": "01/01/2022",
//            "dateFin": "02/01/2022",
//            "datePaiement": "03/01/2022",
//            "distributionList": [
//                {
//                    "distributeur": "Distributor A",
//                    "lieu": "Location A",
//                    "dateDebut": "04/01/2022 15:45",
//                    "dateFin": "04/01/2022 14:45"
//                }
//            ]
//        }
//        """;
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(evenementJson)
//                .when()
//                .post("/evenement")
//                .then()
//                .statusCode(400);
//    }
//
//    @Test
//    void testGetAll(){
//
//    }
}