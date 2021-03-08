Feature: Movies
  Scenario: Get Movie Details
    Given I am already logged into the API
    When A user sends a request to get the last Movie Id
    Then A new sends a request to get the Movie Details

  Scenario: Rate Movie
    Given I am already logged into the API
    When A user sends a request to get the last Movie Id
    Then The user sends request to Rate the Movie
