@booking @update
Feature: Change a booking
  As a hotel administrator
  I want to change a guest's booking
  So that the reservation reflects the guest's new plans

  Background:
    Given room 1 is already booked for a stay

  @smoke @positive
  Scenario: Administrator moves a booking to new dates
    When the administrator moves the booking to new dates
    Then the booking is updated
    And the booking shows the new details