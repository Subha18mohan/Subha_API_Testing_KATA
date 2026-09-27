package com.booking.stepdefinitions;

import com.booking.api.AuthClient;
import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;

/** Cucumber hooks that run around every scenario. */
@RequiredArgsConstructor
public class Hooks {

    private final ScenarioContext context;
    private final AuthClient authClient;
    private final BookingClient bookingClient;

    /**
     * Logs in as administrator once before every booking scenario, so the steps
     * and the clean-up share one token instead of each logging in on its own.
     */
    @Before("@booking")
    public void logInAsAdministrator() {
        context.setToken(authClient.adminToken());
    }

    /**
     * Deletes the bookings this scenario created, so test data does not pile up
     * on the shared demo site or block the same dates for later runs.
     */
    @After(value = "@booking", order = 20)
    public void deleteBookingsCreatedByScenario() {
        context.getCreatedBookingIds().forEach(id -> bookingClient.delete(id, context.getToken()));
    }

    /**
     * Clears the token once the clean-up has used it. For @After hooks a higher
     * order runs first, so this (order 10) runs after the clean-up (order 20).
     */
    @After(value = "@booking", order = 10)
    public void clearToken() {
        context.setToken(null);
    }
}