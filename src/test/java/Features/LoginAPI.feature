Feature: Test the login API

  @LoginAPI
  Scenario Outline: Check new user is created successfully using API
    When User enters "<username>" and "<password>" in user login API
    Then add a new facility
    Examples:
      | username        | password    |
      | admin@gamil.com | Testing@123 |

