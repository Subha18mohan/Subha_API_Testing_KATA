package com.booking.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BookingClient {
    public Response create(Object booking) {
        return request(null)
                .body(booking)
                .when()
                .post(Endpoints.BOOKINGS);
    }

    public Response getById(int bookingId, String token) {
        return request(token)
                .pathParam("id", bookingId)
                .when()
                .get(Endpoints.BOOKING_BY_ID);
    }

    public Response delete(int bookingId, String token) {
        return request(token)
                .pathParam("id", bookingId)
                .when()
                .delete(Endpoints.BOOKING_BY_ID);
    }

    private RequestSpecification request(String token) {
        RequestSpecification request = given().spec(RequestSpecFactory.baseSpec());
        return token == null ? request : request.cookie("token", token);
    }
}
