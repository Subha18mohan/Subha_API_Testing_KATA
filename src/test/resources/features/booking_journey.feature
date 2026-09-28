@booking @e2e
Feature: Booking journey
  As a hotel administrator
  I want to manage a guest's booking from start to finish
  So that reservations stay accurate throughout the guest's stay

  @smoke @positive
  Scenario: A booking is made, viewed, changed and cancelled
    When the administrator logs in with valid credentials
    Then the login is successful
    And an authentication token is issued
    Given a guest has prepared a valid booking for room 1
    When the guest submits the booking
    Then the booking is confirmed
    When the administrator looks up the booking
    Then the booking details are shown
    And they match what the guest booked
    When the administrator moves the booking to new dates
    Then the booking is updated
    And the booking shows the new details
    When the administrator cancels the booking
    Then the booking is cancelled
    And the booking can no longer be found