package com.booking.stepdefinitions;

import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import com.booking.data.BookingDataFactory;
import com.booking.model.Booking;
import com.booking.support.BookingAssertions;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateBookingSteps {

    private static final int HTTP_OK = 200;

    private final ScenarioContext context;
    private final BookingClient bookingClient;

    @When("the administrator moves the booking to new dates")
    public void theAdministratorMovesTheBookingToNewDates() {
        Booking changed = context.getPreparedBooking().toBuilder()
                .bookingdates(BookingDataFactory.randomFutureStay())
                .build();
        update(changed, context.getToken());
    }

    @Then("the booking is updated")
    public void theBookingIsUpdated() {
        context.getLastResponse().then().statusCode(HTTP_OK);
    }

    @Then("the booking shows the new details")
    public void theBookingShowsTheNewDetails() {
        Booking stored = bookingClient.getById(context.lastCreatedBookingId(), context.getToken())
                .then().statusCode(HTTP_OK)
                .extract().as(Booking.class);
        BookingAssertions.assertSameDetails(context.getPreparedBooking(), stored);
    }

    private void update(Booking changed, String token) {
        context.setPreparedBooking(changed);
        context.setLastResponse(bookingClient.update(context.lastCreatedBookingId(), changed, token));
    }
}