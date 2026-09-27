package com.booking.stepdefinitions;

import com.booking.data.BookingField;
import io.cucumber.java.ParameterType;

/**
 * Custom Cucumber parameter types. They turn words in a feature file into Java
 * objects before the step method is called.
 */
public class ParameterTypes {

    /**
     * {bookingField} matches a booking field by its business name, e.g.
     * "first name", and hands the step the matching BookingField enum.
     */
    @ParameterType("first name|last name|email address|phone number")
    public BookingField bookingField(String businessName) {
        return BookingField.fromBusinessName(businessName);
    }
}