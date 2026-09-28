package com.booking.api;

import com.booking.config.ApiConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private static final int HTTP_OK = 200;

    public Response login(String username, String password) {
        return given()
                .spec(RequestSpecFactory.baseSpec())
                .body("""
                        {"username": "%s", "password": "%s"}
                        """.formatted(username, password))
                .when()
                .post(Endpoints.LOGIN);
    }

    public String adminToken() {
        ApiConfig config = ApiConfig.get();
        return login(config.username(), config.password())
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .path("token");
    }
}
