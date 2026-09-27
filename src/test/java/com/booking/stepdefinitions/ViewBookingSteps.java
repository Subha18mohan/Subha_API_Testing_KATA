package com.booking.stepdefinitions;

import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import com.booking.model.Booking;
import com.booking.support.BookingAssertions;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.anyOf;

/** Step definitions for viewing a booking. */
@RequiredArgsConstructor
public class ViewBookingSteps {

    private static final int HTTP_OK = 200;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_FORBIDDEN = 403;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int UNKNOWN_BOOKING_ID = 999_999;
    private static final String INVALID_TOKEN = "not-a-valid-token";

    private final ScenarioContext context;
    private final BookingClient bookingClient;

    @When("the administrator looks up the booking")
    public void theAdministratorLooksUpTheBooking() {
        lookUp(context.lastCreatedBookingId(), context.getToken());
    }

    @When("someone looks up the booking without logging in")
    public void someoneLooksUpTheBookingWithoutLoggingIn() {
        lookUp(context.lastCreatedBookingId(), null);
    }

    @When("someone looks up the booking with an invalid token")
    public void someoneLooksUpTheBookingWithAnInvalidToken() {
        lookUp(context.lastCreatedBookingId(), INVALID_TOKEN);
    }

    @When("the administrator looks up a booking that does not exist")
    public void theAdministratorLooksUpABookingThatDoesNotExist() {
        lookUp(UNKNOWN_BOOKING_ID, context.getToken());
    }

    @Then("the booking details are shown")
    public void theBookingDetailsAreShown() {
        context.getLastResponse().then()
                .statusCode(HTTP_OK)
                .body("bookingid", equalTo(context.lastCreatedBookingId()));
    }

    @Then("they match what the guest booked")
    public void theyMatchWhatTheGuestBooked() {
        Booking shown = context.getLastResponse().as(Booking.class);
        BookingAssertions.assertSameDetails(context.getPreparedBooking(), shown);
    }

    @Then("access to the booking is denied")
    public void accessToTheBookingIsDenied() {
        context.getLastResponse().then()
                .statusCode(anyOf(equalTo(HTTP_UNAUTHORIZED), equalTo(HTTP_FORBIDDEN)));
    }

    @Then("the booking is reported as not found")
    public void theBookingIsReportedAsNotFound() {
        context.getLastResponse().then().statusCode(HTTP_NOT_FOUND);
    }

    /** Looks up a booking by id with the given token (null = not logged in). */
    private void lookUp(int bookingId, String token) {
        context.setLastResponse(bookingClient.getById(bookingId, token));
    }
}