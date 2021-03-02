Feature: Authentication
  Scenario: Create Token
    Given A have the api key
    When The user sends a request to the authentication endpoint
    Then The a new token is generated