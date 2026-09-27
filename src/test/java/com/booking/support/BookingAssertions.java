package com.booking.support;

import com.booking.model.Booking;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Reusable checks on bookings, shared by all booking step classes so the same
 * comparison is never written twice.
 */
public final class BookingAssertions {

    private BookingAssertions() {
        // static helper - not meant to be instantiated
    }

    /**
     * Checks that the booking returned by the API holds the details that were
     * sent. Email and phone are not compared: the API does not return them
     * (reported as an observation). All differences are reported together.
     */
    public static void assertSameDetails(Booking expected, Booking actual) {
        assertAll("booking details",
                () -> assertEquals(expected.roomid(), actual.roomid(), "room"),
                () -> assertEquals(expected.firstname(), actual.firstname(), "first name"),
                () -> assertEquals(expected.lastname(), actual.lastname(), "last name"),
                () -> assertEquals(expected.depositpaid(), actual.depositpaid(), "deposit paid"),
                () -> assertEquals(expected.bookingdates(), actual.bookingdates(), "dates"));
    }
}