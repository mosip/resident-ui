@regression @getInformation
Feature: Get Information

  @smoke
  Scenario: Search and download registration centers for all location hierarchy levels
    When the user navigates to Get Information
    And the user searches and downloads registration centers for all hierarchy levels
    Then all registration centers should be downloaded successfully
