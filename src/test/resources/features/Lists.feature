Feature: Lists
  Scenario: Create a new list
    Given I am already login into the API
    When A user sends a request to the Create List endpoint
    Then A new list is Created
