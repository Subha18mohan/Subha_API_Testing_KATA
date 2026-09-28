package com.booking.model;

import java.time.LocalDate;

public record BookingDates(LocalDate checkin, LocalDate checkout) {
    public static BookingDates of(LocalDate checkin, int nights) {
        return new BookingDates(checkin, checkin.plusDays(nights));
    }
}
