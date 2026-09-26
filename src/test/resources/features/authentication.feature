Feature: Admin authentication
  As a hotel administrator
  I want to log in with my credentials
  so that only I can view and manage guest bookings

  Scenario: Administrator logs in with valid credentials
    When the administrator logs in with valid credentials
    Then the login is successful
    And an authentication token is issued
