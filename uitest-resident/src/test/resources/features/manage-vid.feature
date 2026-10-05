@regression @manageVid
Feature: Manage My VID

  Background:
    Given the user is logged in with their VID

  @smoke
  Scenario: Regenerate Perpetual VID and decline card download
    When the user navigates to Manage My VID
    And the user regenerates the Perpetual VID
    And the user declines downloading the Perpetual VID card

  Scenario: Generate, download and delete a Temporary VID
    When the user navigates to Manage My VID
    And the user generates a Temporary VID
    Then a success popup should confirm VID creation
    When the user downloads the Temporary VID card
    Then a success popup should confirm the download
    When the user deletes the Temporary VID
    Then a success popup should confirm VID deletion

  Scenario: Generate, download and delete a One-Time VID
    When the user navigates to Manage My VID
    And the user generates a One-Time VID
    Then a success popup should confirm VID creation
    When the user downloads the One-Time VID card
    Then a success popup should confirm the download
    When the user deletes the One-Time VID
    Then a success popup should confirm VID deletion
