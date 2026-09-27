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