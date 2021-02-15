package com.trendyol.step;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static constans.Endpoints.BOOKS;
import static io.restassured.RestAssured.given;

public class DeleteBookQueryParam {

    @Test
    public void deleteRequest() {
        Response response = given()
                .contentType("application/json")
                .when()
                .delete(BOOKS + "/1")
                .then().log().all()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        //  Assertions.assertEquals("2", response.jsonPath().getString("id"));

    }
}
