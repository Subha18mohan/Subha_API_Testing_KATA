package com.booking.context;

import com.booking.model.Booking;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class ScenarioContext {
    private Response lastResponse;

    private Booking preparedBooking;

    private String token;

    private final List<Integer> createdBookingIds = new ArrayList<>();

    public void rememberCreatedBooking(int bookingId) {
        createdBookingIds.add(bookingId);
    }

    public int lastCreatedBookingId() {
        return createdBookingIds.getLast();
    }
}
