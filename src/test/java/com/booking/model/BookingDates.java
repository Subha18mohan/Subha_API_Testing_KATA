package com.booking.model;

import java.time.LocalDate;

/**
 * Check-in and check-out dates of a booking, sent as
 * {"checkin":"2027-03-10","checkout":"2027-03-12"}.
 *
 * @param checkin  first night of the stay
 * @param checkout day the guest leaves
 */
public record BookingDates(LocalDate checkin, LocalDate checkout) {

    /** Convenience factory: a stay of the given number of nights starting on checkin. */
    public static BookingDates of(LocalDate checkin, int nights) {
        return new BookingDates(checkin, checkin.plusDays(nights));
    }
}