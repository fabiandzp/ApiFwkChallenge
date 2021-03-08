Feature: Authentication
  Scenario: Create Token
    Given A have the api key
    When A user sends a request to the authentication endpoint
    Then A new token is generated

  Scenario: Token Validation
    Given A have the api key
    And Token already generated
    When A user sends a request to validate token endpoint
    Then The Token is validated successfully

  Scenario: Generate Session Id
    Given A have the api key
    And Token already generated
    And The Token is validated successfully
    When A user sends a request to generate session id endpoint
    Then The Session Id is generated successfully