package com.trendyol.step;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static constans.Endpoints.BOOKS;
import static io.restassured.RestAssured.given;

public class GetBookList {

    @Test
    public void getBookList() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get(BOOKS)
                .then()
                .log().all().extract().response();
        Assertions.assertEquals(200, response.statusCode());
       //Assertions.assertEquals("Chief Metrics Officer", response.jsonPath().getString("title[1]"));



    }
}
