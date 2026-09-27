package com.booking.stepdefinitions;

import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import com.booking.data.BookingDataFactory;
import com.booking.data.BookingField;
import com.booking.model.Booking;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


/** Step definitions for creating bookings. */
@RequiredArgsConstructor
public class BookingSteps {

    private static final int HTTP_CREATED = 201;

    private final ScenarioContext context;
    private final BookingClient bookingClient;

    @Given("a guest has prepared a valid booking for room {int}")
    public void aGuestHasPreparedAValidBookingForRoom(int roomId) {
        context.setPreparedBooking(BookingDataFactory.validBooking(roomId));
    }

    @Given("the guest enters {string} as the {bookingField}")
    public void theGuestEntersAsThe(String value, BookingField field) {
        context.setPreparedBooking(field.applyTo(context.getPreparedBooking(), value));
    }

    @When("the guest submits the booking")
    public void theGuestSubmitsTheBooking() {
        submit(context.getPreparedBooking());
    }

    @Then("the booking is confirmed")
    public void theBookingIsConfirmed() {
        context.getLastResponse().then()
                .statusCode(HTTP_CREATED)
                .body("bookingid", notNullValue());
    }

    @Then("the confirmation shows the details the guest submitted")
    public void theConfirmationShowsTheDetailsTheGuestSubmitted() {
        Booking sent = context.getPreparedBooking();
        Booking confirmed = context.getLastResponse().as(Booking.class);
        assertAll("booking confirmation",
                () -> assertEquals(sent.roomid(), confirmed.roomid(), "room"),
                () -> assertEquals(sent.firstname(), confirmed.firstname(), "first name"),
                () -> assertEquals(sent.lastname(), confirmed.lastname(), "last name"),
                () -> assertEquals(sent.depositpaid(), confirmed.depositpaid(), "deposit paid"),
                () -> assertEquals(sent.bookingdates(), confirmed.bookingdates(), "dates"));
    }

    /** Sends the booking, stores the response and remembers the id for clean-up. */
    private Response submit(Booking booking) {
        Response response = bookingClient.create(booking);
        context.setLastResponse(response);
        if (response.statusCode() == HTTP_CREATED) {
            context.rememberCreatedBooking(response.path("bookingid"));
        }
        return response;
    }
}