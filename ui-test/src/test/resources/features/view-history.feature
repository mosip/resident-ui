@regression @viewHistory
Feature: View My History

  Background:
    Given the user is logged in with their VID

  @smoke
  Scenario: Search history with a from date
    When the user navigates to View My History
    And the user sets a from date and searches

  Scenario: Filter history by each service type
    When the user navigates to View My History
    And the user sets a from date and searches
    And the user filters by history type "ALL"
    And the user filters by history type "AUTHENTICATION_REQUEST"
    And the user filters by history type "SERVICE_REQUEST"
    And the user filters by history type "DATA_UPDATE_REQUEST"
    And the user filters by history type "ID_MANAGEMENT_REQUEST"
    And the user filters by history type "DATA_SHARE_REQUEST"

  Scenario: Filter history by status and download
    When the user navigates to View My History
    And the user sets a from date and searches
    And the user filters by status "SUCCESS"
    And the user filters by status "IN_PROGRESS"
    And the user filters by status "FAILED"
    When the user downloads the history results
    Then the download button should be visible
