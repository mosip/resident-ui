@regression @personalisedCard
Feature: Get Personalised Card

  Background:
    Given the user is logged in with their VID

  @smoke
  Scenario: Download a personalised card with basic fields
    When the user navigates to Get Personalised Card
    And the user selects basic card fields
    And the user downloads the personalised card
    Then the personalised card download should be confirmed

  Scenario: Download a personalised card with all fields
    When the user navigates to Get Personalised Card
    And the user selects all card fields
    And the user downloads the personalised card
    Then the personalised card download should be confirmed
