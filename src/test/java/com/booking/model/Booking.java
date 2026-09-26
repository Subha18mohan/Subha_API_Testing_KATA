package com.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/**
 * A room booking as sent to and returned by the /booking endpoint.
 * <p>
 * Wrapper types (Integer, Boolean) are used on purpose so a negative test can
 * leave a field null; null fields are left out of the JSON, which is how a
 * client would "forget" a field.
 * <p>
 * Lombok's @Builder(toBuilder = true) lets a test take a valid booking and
 * change just one field: valid.toBuilder().email("not-an-email").build()
 */
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Booking(
        Integer bookingid,
        Integer roomid,
        String firstname,
        String lastname,
        Boolean depositpaid,
        BookingDates bookingdates,
        String email,
        String phone) {
}