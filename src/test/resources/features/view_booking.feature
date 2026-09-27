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