package com.booking.stepdefinitions;

import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

@RequiredArgsConstructor
public class DeleteBookingSteps {
    private static final int HTTP_CREATED = 201;
    private static final int HTTP_ACCEPTED = 202;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int UNKNOWN_BOOKING_ID = 999_999;
    private static final String INVALID_TOKEN = "not-a-valid-token";

    private final ScenarioContext context;
    private final BookingClient bookingClient;

    @When("the administrator cancels the booking")
    public void theAdministratorCancelsTheBooking() {
        cancel(context.lastCreatedBookingId(), context.getToken());
    }

    @When("someone cancels the booking without logging in")
    public void someoneCancelsTheBookingWithoutLoggingIn() {
        cancel(context.lastCreatedBookingId(), null);
    }

    @When("someone cancels the booking with an invalid token")
    public void someoneCancelsTheBookingWithAnInvalidToken() {
        cancel(context.lastCreatedBookingId(), INVALID_TOKEN);
    }

    @When("the administrator cancels a booking that does not exist")
    public void theAdministratorCancelsABookingThatDoesNotExist() {
        cancel(UNKNOWN_BOOKING_ID, context.getToken());
    }

    @Then("the booking is cancelled")
    public void theBookingIsCancelled() {
        context.getLastResponse().then()
                .statusCode(anyOf(equalTo(HTTP_CREATED), equalTo(HTTP_ACCEPTED)));
    }

    @Then("the booking can no longer be found")
    public void theBookingCanNoLongerBeFound() {
        bookingClient.getById(context.lastCreatedBookingId(), context.getToken())
                .then()
                .statusCode(HTTP_NOT_FOUND);
    }

    private void cancel(int bookingId, String token) {
        context.setLastResponse(bookingClient.delete(bookingId, token));
    }
}