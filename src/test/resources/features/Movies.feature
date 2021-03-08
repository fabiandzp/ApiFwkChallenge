Feature: Movies
  Scenario: Get Movie Details
    Given I am already login into the API
    When A user sends a request to the Create List endpoint
    Then A new list is Created

  Scenario: Rate Movie
    Given I am already login into the API
    When A user sends a request to the Get Details endpoint
    Then The response shows the list id description and created by fields
