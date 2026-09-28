@booking @delete
Feature: Cancel a booking
  As a hotel administrator
  I want to cancel a guest's booking
  So that the room becomes available again

  Background:
    Given room 1 is already booked for a stay

  @smoke @positive
  Scenario: Administrator cancels a booking
    When the administrator cancels the booking
    Then the booking is cancelled
    And the booking can no longer be found

  @negative @security
  Scenario: A booking cannot be cancelled without logging in
    When someone cancels the booking without logging in
    Then access to the booking is denied

  @negative @security
  Scenario: A booking cannot be cancelled with an invalid token
    When someone cancels the booking with an invalid token
    Then access to the booking is denied

  @negative
  Scenario: Cancelling a booking that does not exist
    When the administrator cancels a booking that does not exist
    Then the booking is reported as not found