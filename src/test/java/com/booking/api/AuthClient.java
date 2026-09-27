package com.booking.api;

import com.booking.config.ApiConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Talks to the login endpoint. Returns the raw Response so that step
 * definitions decide what to check (a test may expect a login to fail).
 */
public class AuthClient {

    private static final int HTTP_OK = 200;

    /** Sends POST /auth/login with the given user name and password. */
    public Response login(String username, String password) {
        return given()
                .spec(RequestSpecFactory.baseSpec())
                .body("""
                        {"username": "%s", "password": "%s"}
                        """.formatted(username, password))
                .when()
                .post(Endpoints.LOGIN);
    }

    /**
     * Logs in as the administrator and returns the token. Used by booking steps
     * that need to be logged in; fails immediately with a clear message if the
     * login itself does not work.
     */
    public String adminToken() {
        ApiConfig config = ApiConfig.get();
        return login(config.username(), config.password())
                .then()
                .statusCode(HTTP_OK)
                .extract()
                .path("token");
    }
}