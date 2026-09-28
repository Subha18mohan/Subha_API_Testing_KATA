package com.booking.data;

import com.booking.model.Booking;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum BookingField {
    FIRST_NAME("first name", (booking, value) -> booking.toBuilder().firstname(value).build()),
    LAST_NAME("last name", (booking, value) -> booking.toBuilder().lastname(value).build()),
    EMAIL("email address", (booking, value) -> booking.toBuilder().email(value).build()),
    PHONE("phone number", (booking, value) -> booking.toBuilder().phone(value).build());

    private final String businessName;
    private final BiFunction<Booking, String, Booking> setter;

    BookingField(String businessName, BiFunction<Booking, String, Booking> setter) {
        this.businessName = businessName;
        this.setter = setter;
    }

    public Booking applyTo(Booking booking, String value) {
        return setter.apply(booking, value);
    }

    public static BookingField fromBusinessName(String name) {
        return Arrays.stream(values())
                .filter(field -> field.businessName.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown booking field: " + name));
    }
}
