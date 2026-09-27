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

  @negative @validation
  Scenario Outline: Booking is rejected when the <field> is <problem>
    Given a guest has prepared a valid booking for room 1
    But the guest enters "<value>" as the <field>
    When the guest submits the booking
    Then the booking is rejected as invalid
    And the guest is told "<message>"

    Examples: Values just outside the allowed length
      | field        | problem   | value                           | message                        |
      | first name   | too short | Al                              | size must be between 3 and 18  |
      | first name   | too long  | Abcdefghijklmnopqrs             | size must be between 3 and 18  |
      | last name    | too short | Li                              | size must be between 3 and 30  |
      | last name    | too long  | Abcdefghijklmnopqrstuvwxyzabcde | size must be between 3 and 30  |
      | phone number | too short | 0123456789                      | size must be between 11 and 21 |
      | phone number | too long  | 0123456789012345678901          | size must be between 11 and 21 |

    Examples: Wrongly formatted values
      | field         | problem          | value             | message                             |
      | email address | missing the @    | guest.example.com | must be a well-formed email address |
      | email address | missing a domain | guest@            | must be a well-formed email address |

  @negative @validation
  Scenario Outline: Booking is rejected when the <field> is missing
    Given a guest has prepared a valid booking for room 1
    But the guest leaves out the <field>
    When the guest submits the booking
    Then the booking is rejected as invalid

    Examples: Required details that are enforced
      | field      |
      | first name |
      | last name  |

    # Known bug: the specification marks email and phone as required,
    # but the API accepts a booking without them (201 Created).
    @known-bug
    Examples: Required details that are not enforced
      | field         |
      | email address |
      | phone number  |

  @negative @conflict
  Scenario: A room cannot be booked twice for the same dates
    Given room 1 is already booked for a stay
    When another guest books room 1 for the same dates
    Then the booking is refused because the room is already taken