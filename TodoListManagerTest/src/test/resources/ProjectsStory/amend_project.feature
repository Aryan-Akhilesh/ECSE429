Feature: Amend project fields
  As a user, I want to be able to amend the title field of a project

  Background:
    Given dummy Project to be amended


  Scenario Outline: amend project fields using POST
    Given http://localhost:4567/projects/:id
    When I instantiate the title field with "<title>"
    And passes that field in a post request to the Dummy project
    Then amend success code is returned
    And title is modified
    Examples:
      | title |
      | NewTitle |
      | Homework |
      | Software Validation |


  Scenario Outline: amend project fields using PUT
    Given http://localhost:4567/projects/:id
    When I instantiate the title field with "<title>"
    And passes that field in a put request to the Dummy project
    Then amend success code is returned
    And title is modified
    Examples:
      | title |
      | NewTitle |
      | Homework |
      | Software Validation |

  Scenario Outline: amend invalid project fields using PUT
    Given http://localhost:4567/projects/:id
    When I instantiate the topic field with "<topic>"
    And passes that field in a post request to the Dummy project
    Then amend error code is returned
    And amend error message is displayed
    Examples:
      | topic |
      | NewTitle |
      | Homework |
      | Software Validation |
