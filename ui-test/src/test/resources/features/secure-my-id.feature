@regression @secureMyId
Feature: Secure My ID

  Background:
    Given the user is logged in with their VID

  @smoke
  Scenario Outline: Toggle auth lock type <type>
    When the user navigates to Secure My ID
    And the user toggles auth type <type>
    Then the auth lock change should be confirmed
    When the user navigates to Secure My ID
    And the user toggles auth type <type>
    Then the auth lock change should be confirmed

    Examples:
      | type |
      | 1    |
      | 2    |
      | 3    |
      | 4    |
      | 5    |
