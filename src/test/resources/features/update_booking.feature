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


  @negative @security
  Scenario: A booking cannot be changed without logging in
    When someone changes the booking without logging in
    Then access to the booking is denied

  @negative @validation
  Scenario Outline: A change is rejected when the <field> breaks the rules
    When the administrator changes the <field> to "<value>"
    Then the booking is rejected as invalid


    Examples:
      | field        | value      |
      | first name   | Al         |
      | phone number | 0123456789 |

  @positive @known-bug
  Scenario: Administrator changes the guest's name without changing the dates
    When the administrator changes the guest's name but keeps the dates
    Then the booking is updated


  @negative @validation @known-bug
  Scenario: A rejected change explains the broken rule like a rejected booking does
    When the administrator changes the first name to "Al"
    Then the booking is rejected as invalid
    And the guest is told "size must be between 3 and 18"