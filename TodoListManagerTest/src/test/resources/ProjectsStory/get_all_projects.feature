Feature: Get all projects
  As a user, I want to be able to perform get requests to view all the projects stored in the todo manager


  Scenario Outline: Retrieving specific project by ID in JSON
    Given user performs GET request on the url "<url>"
    Then all projects are returned
    And the expected success code is returned
    Examples:
      | url |
      |http://localhost:4567/projects|


  Scenario Outline: Retrieving specific project by ID in XML
    Given user performs GET request on the url "<url>" with xml header
    Then all projects are returned in xml format
    And the expected success code is returned
    Examples:
      | url |
      |http://localhost:4567/projects|


  Scenario Outline: Retrieving specific project with invalid url
    Given user performs GET request on the url "<url>"
    Then the expected error code is returned
    Examples:
      | url |
      |http://localhost:4567/project|