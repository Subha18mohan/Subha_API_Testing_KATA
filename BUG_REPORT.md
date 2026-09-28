# Bug report – Restful Booker Platform API

| | |
|---|---|
| **System under test** | https://automationintesting.online/api |
| **Reference** | Swagger spec in `src/test/resources/spec/booking.yaml` |
| **Tested** | 25–28 September 2026 |
| **Tester** | Subhashini |

Every bug below has a Cucumber scenario tagged `@known-bug` that describes the **correct** behaviour. These scenarios are excluded from the default run so the build stays green, and can be run on their own to reproduce the bugs:

```
mvn clean test -Dcucumber.filter.tags=@known-bug
```

## Summary

| ID | Title | Severity | Scenario |
|---|---|---|---|
| BUG-01 | Partial update of a booking is not implemented | High | update_booking.feature – "Administrator changes only the guest's first name" |
| BUG-02 | A booking cannot be updated without changing its dates | High | update_booking.feature – "Administrator changes the guest's name without changing the dates" |
| BUG-03 | Update validation error exposes internal server details | High | update_booking.feature – "A rejected change explains the broken rule like a rejected booking does" |
| BUG-04 | Check-out before check-in is reported as a conflict | Medium | create_booking.feature – "Booking is rejected when check-out is before check-in" |
| BUG-05 | Email and phone number are not required | Medium | create_booking.feature – "Required details that are not enforced" |

## Bugs

### BUG-01: Partial update of a booking is not implemented
- **Severity:** High
- **Endpoint:** `PATCH /booking/{id}`
- **Steps:**
    1. Log in as admin and create a booking.
    2. Send `PATCH /booking/{id}` with the token cookie and body `{"firstname": "Maria"}`.
- **Expected:** 200, only the first name changes (documented as "Partially update booking").
- **Actual:** 405 Method Not Allowed, with or without a token.

### BUG-02: A booking cannot be updated without changing its dates
- **Severity:** High
- **Endpoint:** `PUT /booking/{id}`
- **Steps:**
    1. Log in as admin and create a booking.
    2. Send `PUT /booking/{id}` with the same dates and a different first name.
- **Expected:** 200, the new name is saved.
- **Actual:** 409 Conflict. The booking clashes with itself, so a guest's details can only be corrected by also moving the stay.

### BUG-03: Update validation error exposes internal server details
- **Severity:** High (security – information disclosure)
- **Endpoint:** `PUT /booking/{id}`
- **Steps:**
    1. Log in as admin and create a booking.
    2. Send `PUT /booking/{id}` with first name `Al` (too short) or a 10-digit phone number.
- **Expected:** 400 with the same readable format as creating a booking, e.g. `{"errors": ["size must be between 3 and 18"]}`.
- **Actual:** 400, but the body contains `errorMessage` with the Spring method signature, internal class names (`com.automationintesting.api.BookingController.updateBooking`) and `java.sql.SQLException`. This reveals the technology stack to anyone, and the format differs from the create endpoint.

### BUG-04: Check-out before check-in is reported as a conflict
- **Severity:** Medium
- **Endpoint:** `POST /booking`
- **Steps:** Send a valid booking where the check-out date is before the check-in date.
- **Expected:** 400 Bad Request explaining the dates are invalid.
- **Actual:** 409 `{"error": "Failed to create booking"}`. The guest is told the room is taken instead of that the dates are wrong.

### BUG-05: Email and phone number are not required
- **Severity:** Medium
- **Endpoint:** `POST /booking`
- **Steps:** Send a valid booking without `email` (and separately without `phone`).
- **Expected:** 400, both fields are marked required in the spec.
- **Actual:** 201, the booking is created and the hotel has no way to contact the guest.

## Observations (differences from the spec)

The API works in these cases, but its behaviour differs from the documentation. The tests follow the real behaviour and accept the documented value where both are reasonable.

| ID | Endpoint | Spec says | API does |
|---|---|---|---|
| OBS-01 | `POST /booking` | 200 with `{bookingid, booking: {...}}` | 201 with a flat booking object; email and phone are not returned |
| OBS-02 | `GET` and `DELETE /booking/{id}` without a token | 401 Unauthorized | 403 Forbidden with an empty body (tests accept both) |
| OBS-03 | `DELETE /booking/{id}` | 201 | 202 Accepted (tests accept both) |
| OBS-04 | `POST /booking` last name | 3–18 characters | 3–30 characters (tests use the real limit) |
| OBS-05 | `POST /booking` same room and dates | 409 not documented | 409 with the generic message "Failed to create booking" |
| OBS-06 | `GET /booking/{id}` unknown id | 404 not documented | 404 with an empty body |
| OBS-07 | `GET /booking/{id}` | Returns the full booking | Email and phone are missing from the response |
| OBS-08 | Validation errors | – | Messages such as "size must be between 3 and 18" do not name the field |

## Test data notes
- The API is shared by all candidates and resets about every 10 minutes.
- A room cannot be booked twice for overlapping dates, so the tests use random future dates for every booking.
- Every booking a scenario creates is deleted afterwards by an `@After` hook.