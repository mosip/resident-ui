@regression @verifyContact
Feature: Verify Phone Number and Email ID

  # This service lives on the pre-login home page (like Get My UIN) — the resident supplies
  # their VID directly on the verify page itself, so no prior login step is needed here.

  Scenario: Attempt email verification with an invalid VID
    When the user navigates to Verify Phone Number and Email ID
    And the user tries to verify email with an invalid VID
    Then the OTP submit button should not appear for email

  Scenario: Attempt phone verification with an invalid VID
    When the user navigates to Verify Phone Number and Email ID
    And the user tries to verify phone with an invalid VID
    Then the OTP submit button should not appear for phone

  @smoke
  Scenario: Verify email with a valid VID and OTP
    When the user navigates to Verify Phone Number and Email ID
    And the user verifies their email with a valid OTP
    Then the email verification should be confirmed
