@auth
Feature: Admin authentication
  As a hotel administrator
  I want to log in with my credentials
  So that only I can view and manage guest bookings

  @smoke @positive
  Scenario: Administrator logs in with valid credentials
    When the administrator logs in with valid credentials
    Then the login is successful
    And an authentication token is issued

  @negative
  Scenario Outline: Login is refused for <case>
    When someone logs in with username "<username>" and password "<password>"
    Then the login is refused
    And the reason given is "Invalid credentials"
    And no authentication token is issued

    Examples:
      | case                     | username | password |
      | a wrong password         | admin    | wrong    |
      | an unknown user          | nobody   | password |
      | a username in wrong case | ADMIN    | password |
      | a password in wrong case | admin    | PASSWORD |