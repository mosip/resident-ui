@regression @getMyUin
Feature: Get My UIN

  @smoke
  Scenario: Download UIN card with valid VID and OTP
    When the user navigates to Get My UIN
    And the user requests the UIN card with their VID and OTP
    Then the UIN card download should be confirmed

  Scenario: Attempt to get UIN without entering a VID
    When the user navigates to Get My UIN
    And the user attempts to get UIN without entering a VID
    Then the get UIN submission button should not appear

  Scenario: Attempt to get UIN with an invalid OTP
    When the user navigates to Get My UIN
    And the user requests UIN with an invalid OTP
    And the portal is dismissed after UIN error
