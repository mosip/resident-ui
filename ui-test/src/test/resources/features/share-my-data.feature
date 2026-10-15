  @regression @shareMyData
Feature: Share My Data

  Background:
    Given the user is logged in with their VID

  @smoke
  Scenario: Share basic data fields with a partner
    When the user navigates to Share My Data
    And the user selects basic data fields and a partner
    And the user confirms the data share
    Then the data share should be confirmed

  Scenario: Share all data fields with a partner
    When the user navigates to Share My Data
    And the user selects all data fields and a partner
    And the user confirms the data share
    Then the data share should be confirmed
