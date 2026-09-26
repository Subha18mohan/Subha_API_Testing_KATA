package com.booking.data;

import com.booking.model.Booking;
import com.booking.model.BookingDates;

import java.time.LocalDate;
import java.util.random.RandomGenerator;

/**
 * Creates valid, unique bookings for tests (Test Data Factory).
 * <p>
 * The API rejects a booking whose dates overlap an existing booking for the same
 * room, and the demo site is shared by many people. Each booking therefore gets
 * a random future check-in date and random names, so runs do not collide.
 */
public final class BookingDataFactory {

    /** A room that exists in the demo data after every reset. */
    public static final int DEFAULT_ROOM_ID = 1;

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private BookingDataFactory() {
        // static factory - not meant to be instantiated
    }

    /** @return a booking for the default room that passes every validation rule */
    public static Booking validBooking() {
        return validBooking(DEFAULT_ROOM_ID);
    }

    /** @return a valid booking for the given room */
    public static Booking validBooking(int roomId) {
        String firstname = "Test" + randomLetters(6);
        String lastname = "Guest" + randomLetters(6);
        return Booking.builder()
                .roomid(roomId)
                .firstname(firstname)
                .lastname(lastname)
                .depositpaid(true)
                .bookingdates(randomFutureStay())
                .email("%s.%s@example.com".formatted(firstname, lastname).toLowerCase())
                .phone("0" + randomDigits(11))
                .build();
    }

    /** @return a 2-night stay starting between 30 days and about 5 years from today */
    public static BookingDates randomFutureStay() {
        LocalDate checkin = LocalDate.now().plusDays(RANDOM.nextInt(30, 1800));
        return BookingDates.of(checkin, 2);
    }

    /** @return the given number of random lower-case letters */
    public static String randomLetters(int length) {
        StringBuilder text = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            text.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }
        return text.toString();
    }

    private static String randomDigits(int length) {
        StringBuilder digits = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            digits.append(RANDOM.nextInt(10));
        }
        return digits.toString();
    }
}