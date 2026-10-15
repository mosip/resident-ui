@regression @login
Feature: Login

  # The only scenarios that always perform a genuine OTP login — every other feature's login
  # step reuses a cached session once one exists, so these use a separate "fresh login" step
  # that never checks that cache.

  @smoke
  Scenario: Login with VID
    Given the user performs a fresh login with their VID
    Then the login should be confirmed

  @smoke
  Scenario: Login with UIN
    Given the user performs a fresh login with their UIN
    Then the login should be confirmed
