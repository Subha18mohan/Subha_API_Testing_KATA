package com.booking.data;

import com.booking.model.Booking;
import com.booking.model.BookingDates;

import java.time.LocalDate;
import java.util.random.RandomGenerator;

public final class BookingDataFactory {
    public static final int DEFAULT_ROOM_ID = 1;

    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private BookingDataFactory() {
    }

    public static Booking validBooking() {
        return validBooking(DEFAULT_ROOM_ID);
    }

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

    public static BookingDates randomFutureStay() {
        LocalDate checkin = LocalDate.now().plusDays(RANDOM.nextInt(30, 1800));
        return BookingDates.of(checkin, 2);
    }

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
