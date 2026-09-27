@booking @view
Feature: View a booking
  As a hotel administrator
  I want to look up a guest's booking
  So that I can check the reservation details

  Background:
    Given room 1 is already booked for a stay

  @smoke @positive
  Scenario: Administrator views an existing booking
    When the administrator looks up the booking
    Then the booking details are shown
    And they match what the guest booked


  @negative @security
  Scenario: A booking cannot be viewed without logging in
    When someone looks up the booking without logging in
    Then access to the booking is denied

  @negative @security
  Scenario: A booking cannot be viewed with an invalid token
    When someone looks up the booking with an invalid token
    Then access to the booking is denied

  @negative
  Scenario: Looking up a booking that does not exist
    When the administrator looks up a booking that does not exist
    Then the booking is reported as not found