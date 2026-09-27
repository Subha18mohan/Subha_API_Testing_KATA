package com.booking.context;

import com.booking.model.Booking;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;

/**
 * Data shared by all step definition classes during ONE scenario.
 * <p>
 * Cucumber's PicoContainer creates a new instance for every scenario and passes
 * the same instance to every step class that asks for it, so steps in different
 * classes can hand data to each other while scenarios stay isolated.
 * A plain Java object (POJO): no framework annotations, no inheritance.
 */
@Getter
@Setter
public class ScenarioContext {

    /** Response of the most recent API call made by a step. */
    private Response lastResponse;
    /** Booking prepared by a Given step and sent by a When step. */
    private Booking preparedBooking;
}