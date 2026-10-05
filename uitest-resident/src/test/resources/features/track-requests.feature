@regression @trackRequests
Feature: Track My Requests

  Background:
    Given the user is logged in with their VID

  @smoke
  Scenario: Track a valid event ID
    When the user generates a Temporary VID to get an event ID
    And the user tracks the generated event ID
    Then the event ID details should be visible
    And the user downloads the acknowledgement

  Scenario: Track an invalid event ID
    When the user tracks an invalid event ID
    Then the event ID details should not be visible
