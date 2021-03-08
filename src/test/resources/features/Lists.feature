Feature: Lists
  Scenario: Create a new list
    Given I am already login into the API
    When A user sends a request to the Create List endpoint
    Then A new list is Created

  Scenario: Get List Details
    Given I am already login into the API
    When A user sends a request to the Get Details endpoint
    Then The response shows the list id description and created by fields

  Scenario: Add Movie to the list
    Given I am already login into the API
    And A user sends a request to the Create List endpoint
    When A user sends a request to Add Movie endpoint
    Then The movie listed in the List Details endpoint

  Scenario: Clear the List
    Given I am already login into the API
    And A user sends a request to the Create List endpoint
    And A user sends a request to Add Movie endpoint
    When The user sends request to Clear List
    Then The List is empty

  Scenario: Delete the list
    Given I am already login into the API
    And A user sends a request to the Create List endpoint
    And A user sends a request to Add Movie endpoint
    And The user sends request to Clear List
    When The user sends request to Delete List
    Then The List is Deleted



