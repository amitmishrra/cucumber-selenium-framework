Feature: End to End flows for rekap functionality

  Background:
    Given User is in login page

  @opt-in
  Scenario Outline: Surgeon Opt-In Flow
    When User enters "<adminMail>" and "<password>" in the login page

    Examples:
      | adminMail                       | password    |
      | fake-user+admin@kaliberlabs.com | Testing@123 |