Feature: Tv
  Scenario: Get TV show Details
    Given I am already logged at the API
    When A user sends a request to get the last Tv show Id
    Then A user sends a request to get the Tv Details
