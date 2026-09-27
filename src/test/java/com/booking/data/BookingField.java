package com.booking.data;

import com.booking.model.Booking;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * The booking fields a guest fills in, by their business name as used in the
 * feature files ("first name", "phone number", ...). Each constant knows how to
 * put a value into a copy of a booking.
 * <p>
 * One small lambda per field instead of a large switch keeps every method
 * simple (low cyclomatic complexity). A null value removes the field, which is
 * how a scenario sends a booking with that field missing.
 */
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

    /** @return a copy of the booking with this field set to the given value */
    public Booking applyTo(Booking booking, String value) {
        return setter.apply(booking, value);
    }

    /** @return the field with the given business name, e.g. "first name" */
    public static BookingField fromBusinessName(String name) {
        return Arrays.stream(values())
                .filter(field -> field.businessName.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown booking field: " + name));
    }
}