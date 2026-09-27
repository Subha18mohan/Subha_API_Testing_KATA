package com.booking.stepdefinitions;

import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import com.booking.model.Booking;
import com.booking.support.BookingAssertions;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static org.hamcrest.Matchers.equalTo;

/** Step definitions for viewing a booking. */
@RequiredArgsConstructor
public class ViewBookingSteps {

    private static final int HTTP_OK = 200;

    private final ScenarioContext context;
    private final BookingClient bookingClient;

    @When("the administrator looks up the booking")
    public void theAdministratorLooksUpTheBooking() {
        lookUp(context.lastCreatedBookingId(), context.getToken());
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

    /** Looks up a booking by id with the given token (null = not logged in). */
    private void lookUp(int bookingId, String token) {
        context.setLastResponse(bookingClient.getById(bookingId, token));
    }
}