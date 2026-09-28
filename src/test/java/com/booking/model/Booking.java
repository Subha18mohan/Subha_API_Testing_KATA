package com.booking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

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
