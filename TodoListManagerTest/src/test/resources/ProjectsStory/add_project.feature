Feature: add new project
  As a user, I want to add a new project to manage my work.

  Scenario Outline: Add a project with all fields
    Given the URL https://localhost:4567/projects
    When user creates a project with "<title>", "<completed>", "<active>" and "<description>"
    And passes that project in a post request
    Then success error code is returned
    And object body is returned
    Examples:
      | title |completed | active | description |
      | Rainbow | false | true | Rain |
      | planting | false | false | soil|
      | Homework | true | true | ECSE429|
      | ClassTest| false| false| ECSE429|

  Scenario Outline: Add a project without fields
    Given the URL https://localhost:4567/projects
    When user creates a project
    And passes that project in a post request
    Then success error code is returned
    And object body is returned
    Examples:
      |  |
    #Since this task is adding a project without fields, there should have no parameters

  Scenario Outline: Add a project with invalid field
    Given the URL https://localhost:4567/projects
    When user creates a project with "<doneStatus>"
    And passes that project in a post request
    Then error code displayed
    Examples:
      |doneStatus|
      |false     |
      |true      |