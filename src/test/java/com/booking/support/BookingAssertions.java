package com.booking.support;

import com.booking.model.Booking;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class BookingAssertions {
    private BookingAssertions() {
    }

    public static void assertSameDetails(Booking expected, Booking actual) {
        assertAll("booking details",
                () -> assertEquals(expected.roomid(), actual.roomid(), "room"),
                () -> assertEquals(expected.firstname(), actual.firstname(), "first name"),
                () -> assertEquals(expected.lastname(), actual.lastname(), "last name"),
                () -> assertEquals(expected.depositpaid(), actual.depositpaid(), "deposit paid"),
                () -> assertEquals(expected.bookingdates(), actual.bookingdates(), "dates"));
    }
}
