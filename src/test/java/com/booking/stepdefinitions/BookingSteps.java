package com.booking.stepdefinitions;

import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import com.booking.data.BookingDataFactory;
import com.booking.data.BookingField;
import com.booking.model.Booking;
import com.booking.model.BookingDates;
import com.booking.support.BookingAssertions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

import static org.hamcrest.Matchers.*;

@RequiredArgsConstructor
public class BookingSteps {
    private static final int HTTP_CREATED = 201;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_CONFLICT = 409;

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

    @Given("the guest leaves out the {bookingField}")
    public void theGuestLeavesOutThe(BookingField field) {
        context.setPreparedBooking(field.applyTo(context.getPreparedBooking(), null));
    }

    @Given("room {int} is already booked for a stay")
    public void roomIsAlreadyBookedForAStay(int roomId) {
        Booking existing = BookingDataFactory.validBooking(roomId);
        submit(existing).then().statusCode(HTTP_CREATED);
        context.setPreparedBooking(existing);
    }

    @Given("the check-out date is before the check-in date")
    public void theCheckOutDateIsBeforeTheCheckInDate() {
        Booking booking = context.getPreparedBooking();
        BookingDates dates = booking.bookingdates();
        BookingDates swapped = new BookingDates(dates.checkout(), dates.checkin());
        context.setPreparedBooking(booking.toBuilder().bookingdates(swapped).build());
    }

    @When("the guest submits the booking")
    public void theGuestSubmitsTheBooking() {
        submit(context.getPreparedBooking());
    }

    @When("another guest books room {int} for the same dates")
    public void anotherGuestBooksRoomForTheSameDates(int roomId) {
        Booking other = BookingDataFactory.validBooking(roomId).toBuilder()
                .bookingdates(context.getPreparedBooking().bookingdates())
                .build();
        submit(other);
    }

    @Then("the booking is confirmed")
    public void theBookingIsConfirmed() {
        context.getLastResponse().then()
                .statusCode(HTTP_CREATED)
                .body("bookingid", notNullValue());
    }

    @Then("the confirmation shows the details the guest submitted")
    public void theConfirmationShowsTheDetailsTheGuestSubmitted() {
        Booking confirmed = context.getLastResponse().as(Booking.class);
        BookingAssertions.assertSameDetails(context.getPreparedBooking(), confirmed);
    }

    @Then("the booking is rejected as invalid")
    public void theBookingIsRejectedAsInvalid() {
        context.getLastResponse().then().statusCode(HTTP_BAD_REQUEST);
    }

    @Then("the guest is told {string}")
    public void theGuestIsTold(String message) {
        context.getLastResponse().then().body("errors", hasItem(message));
    }

    @Then("the booking is refused because the room is already taken")
    public void theBookingIsRefusedBecauseTheRoomIsAlreadyTaken() {
        context.getLastResponse().then()
                .statusCode(HTTP_CONFLICT)
                .body("error", equalTo("Failed to create booking"));
    }

    private Response submit(Booking booking) {
        Response response = bookingClient.create(booking);
        context.setLastResponse(response);
        if (response.statusCode() == HTTP_CREATED) {
            context.rememberCreatedBooking(response.path("bookingid"));
        }
        return response;
    }
}
