@regression @updateMyData
Feature: Update My Data

  Background:
    Given the user is logged in with their VID

  @smoke
  Scenario: Update name details
    When the user navigates to Update My Data
    And the user updates their name details
    Then the data update should be confirmed

  Scenario: Update address details
    When the user navigates to Update My Data
    And the user updates their address details
    Then the data update should be confirmed

  Scenario: Update preferred language
    When the user navigates to Update My Data
    And the user updates their preferred language
    Then the data update should be confirmed

  Scenario: Update email address via OTP
    When the user navigates to Update My Data
    And the user updates their email address
    Then the data update should be confirmed
