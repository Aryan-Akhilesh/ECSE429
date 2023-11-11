Feature: Get all projects
  As a user, I want to be able to perform get requests to view all the projects stored in the todo manager


  Scenario: Retrieving specific todo by ID in JSON
    Given user performs GET request on the url: http://localhost:4567/projects/
    Then all projects are returned
    And the expected success code is returned


  Scenario: Retrieving specific todo by ID in XML
    Given user performs GET request on the url: http://localhost:4567/projects/ with xml header
    Then all projects are returned in xml format
    And the expected success code is returned


  Scenario: Retrieving specific todo with invalid url
    Given invalid url
    And user performs GET request on the url
    Then the expected error code is returned