package com.booking.stepdefinitions;

import com.booking.api.AuthClient;
import com.booking.api.BookingClient;
import com.booking.context.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Hooks {
    private final ScenarioContext context;
    private final AuthClient authClient;
    private final BookingClient bookingClient;

    @Before("@booking")
    public void logInAsAdministrator() {
        context.setToken(authClient.adminToken());
    }

    @After(value = "@booking", order = 20)
    public void deleteBookingsCreatedByScenario() {
        context.getCreatedBookingIds().forEach(id -> bookingClient.delete(id, context.getToken()));
    }

    @After(value = "@booking", order = 10)
    public void clearToken() {
        context.setToken(null);
    }
}
