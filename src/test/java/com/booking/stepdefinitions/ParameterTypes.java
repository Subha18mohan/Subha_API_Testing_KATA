package com.booking.stepdefinitions;

import com.booking.data.BookingField;
import io.cucumber.java.ParameterType;

public class ParameterTypes {
    @ParameterType("first name|last name|email address|phone number")
    public BookingField bookingField(String businessName) {
        return BookingField.fromBusinessName(businessName);
    }
}
