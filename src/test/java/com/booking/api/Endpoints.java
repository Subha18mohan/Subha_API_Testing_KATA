package com.booking.api;

/**
 * API paths, relative to base.url. Kept in one place so a path change is a
 * one-line fix. {id} is filled in by Rest-Assured's pathParam("id", ...).
 */
public final class Endpoints {

    public static final String LOGIN = "/auth/login";
    public static final String BOOKINGS = "/booking";
    public static final String BOOKING_BY_ID = "/booking/{id}";

    private Endpoints() {
        // only constants - not meant to be instantiated
    }
}