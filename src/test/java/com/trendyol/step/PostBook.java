package com.trendyol.step;


import io.restassured.response.Response;
import model.BookList;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static constans.Endpoints.BOOKS;
import static io.restassured.RestAssured.given;

public class PostBook {


    @Test
    public void postBookRequest() {

        BookList bookList = new BookList();

        String author = bookList.setAuthor("");
        String title = bookList.setTitle("");

        if (!title.equals(bookList.getTitle()))
            if (!author.equals(bookList.getAuthor()))
                if (!title.isEmpty())
                    if (!author.isEmpty()) {
                        {
                            Response response = given()
                                    .contentType("application/json")
                                    .when().with()
                                    .body(bookList)
                                    .post(BOOKS)
                                    .then().log().all()
                                    .extract().response();

                            Assertions.assertEquals(201, response.statusCode());
                            Assertions.assertEquals(author, response.jsonPath().getString("author"));
                            Assertions.assertEquals(title, response.jsonPath().getString("title"));
                        }

                    }
    }


}
