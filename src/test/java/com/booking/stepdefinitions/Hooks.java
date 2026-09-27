package com.booking.stepdefinitions;

import com.booking.api.AuthClient;
import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import io.cucumber.java.After;
import lombok.RequiredArgsConstructor;

/** Cucumber hooks that run around every scenario. */
@RequiredArgsConstructor
public class Hooks {

    private final ScenarioContext context;
    private final AuthClient authClient;
    private final BookingClient bookingClient;

    /**
     * Deletes the bookings this scenario created, so test data does not pile up
     * on the shared demo site or block the same dates for later runs.
     * Runs after every scenario, whether it passed or failed.
     */
    @After
    public void deleteBookingsCreatedByScenario() {
        if (context.getCreatedBookingIds().isEmpty()) {
            return;
        }
        String token = authClient.adminToken();
        context.getCreatedBookingIds().forEach(id -> bookingClient.delete(id, token));
    }
}