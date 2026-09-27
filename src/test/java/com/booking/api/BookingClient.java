package com.booking.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * One method per operation of the /booking endpoint (API client pattern).
 * Step definitions describe behaviour; this class knows the HTTP details:
 * paths, methods, path parameters and the auth cookie.
 * A null token sends the request without authentication.
 */
public class BookingClient {

    /** POST /booking - no login needed: any guest can book a room. */
    public Response create(Object booking) {
        return request(null)
                .body(booking)
                .when()
                .post(Endpoints.BOOKINGS);
    }

    /** GET /booking/{id} - admin only. */
    public Response getById(int bookingId, String token) {
        return request(token)
                .pathParam("id", bookingId)
                .when()
                .get(Endpoints.BOOKING_BY_ID);
    }

    /** DELETE /booking/{id} - admin only. */
    public Response delete(int bookingId, String token) {
        return request(token)
                .pathParam("id", bookingId)
                .when()
                .delete(Endpoints.BOOKING_BY_ID);
    }

    /** Shared request setup; adds the "token" cookie when a token is given. */
    private RequestSpecification request(String token) {
        RequestSpecification request = given().spec(RequestSpecFactory.baseSpec());
        return token == null ? request : request.cookie("token", token);
    }
}