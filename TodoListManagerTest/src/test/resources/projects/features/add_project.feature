@tag
  Feature: add new project
    As a user, I want to add a new project to manage my work.

  @tag1
  Scenario Outline: Add a project with all fields
    Given the URL https://localhost:4567/projects
    When user creates a project with "<title>", "<completed>", "<active>" and "<description>"
    And passes that project in a post request
    Then success error code is returned
    And object body is returned
    And todo list state is restored by deleting newly created project

    Examples:
      | title |completed | active | description |
      | Rainbow | false | true | Rain |
      | planting | false | false | soil|

  @tag2
  Scenario Outline: Add a project without fields
    Given the URL https://localhost:4567/projects
    When user creates a project
    And passes that project in a post request
    Then success error code is returned
    And object body is returned
    And todo list state is restored by deleting newly created project

    Examples:
    ||
    ||

  @tag3
  Scenario Outline: Add a project with invalid field
    Given the URL https://localhost:4567/projects
    When user creates a project with "<doneStatus>"
    And passes that project in a post request
    Then error code displayed
    And error message is returned

    Examples:
      |doneStatus|
      |false     |
