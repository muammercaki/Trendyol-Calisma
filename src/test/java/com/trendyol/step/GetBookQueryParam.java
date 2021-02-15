package com.trendyol.step;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


import static constans.Endpoints.BOOKS;
import static io.restassured.RestAssured.given;

public class GetBookQueryParam {

    @Test
    public void getBookWithQueryParam() {

        Response response = given()
                .contentType(ContentType.JSON)
                .param("id", "2")
                .when()
                .get(BOOKS)
                .then()
                .log().all()
                .extract().response();
        Assertions.assertEquals(200, response.statusCode());
       // Assertions.assertEquals("John Smith", response.jsonPath().getString("author[0]"));
    }
}
