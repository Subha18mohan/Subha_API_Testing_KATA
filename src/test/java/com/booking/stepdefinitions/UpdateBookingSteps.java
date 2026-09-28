package com.booking.stepdefinitions;

import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import com.booking.data.BookingDataFactory;
import com.booking.data.BookingField;
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
        update(withNewDates(), context.getToken());
    }

    @When("someone changes the booking without logging in")
    public void someoneChangesTheBookingWithoutLoggingIn() {
        update(withNewDates(), null);
    }

    @When("the administrator changes the {bookingField} to {string}")
    public void theAdministratorChangesTheTo(BookingField field, String value) {
        update(field.applyTo(withNewDates(), value), context.getToken());
    }

    @When("the administrator changes the guest's name but keeps the dates")
    public void theAdministratorChangesTheGuestsNameButKeepsTheDates() {
        Booking renamed = context.getPreparedBooking().toBuilder()
                .firstname("Renamed" + BookingDataFactory.randomLetters(4))
                .build();
        update(renamed, context.getToken());
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

    private Booking withNewDates() {
        return context.getPreparedBooking().toBuilder()
                .bookingdates(BookingDataFactory.randomFutureStay())
                .build();
    }
}
