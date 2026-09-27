@booking @create
Feature: Create a room booking
  As a guest of the hotel
  I want to book a room for my travel dates
  So that the room is reserved for me

  @smoke @positive
  Scenario: Guest books a room with valid details
    Given a guest has prepared a valid booking for room 1
    When the guest submits the booking
    Then the booking is confirmed
    And the confirmation shows the details the guest submitted

  @positive @boundary
  Scenario Outline: Booking is accepted when the <field> has <length> characters
    Given a guest has prepared a valid booking for room 1
    But the guest enters "<value>" as the <field>
    When the guest submits the booking
    Then the booking is confirmed

    Examples: Shortest and longest allowed values
      | field        | length | value                          |
      | first name   | 3      | Ann                            |
      | first name   | 18     | Abcdefghijklmnopqr             |
      | last name    | 3      | Lee                            |
      | last name    | 30     | Abcdefghijklmnopqrstuvwxyzabcd |
      | phone number | 11     | 01234567890                    |
      | phone number | 21     | 012345678901234567890          |