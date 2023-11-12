Feature: Delete projects
  As a user, I want to close a project since it is already finished.

  Background:
    Given a dummy Project to be amended

  Scenario Outline: delete a project with specific project id
    Given a url http://localhost:4567/projects/:id where id = "<id>"
    When I delete the project with id
    Then a success code should be returned
    Examples:
      | id |
      | 1  |
      | 2  |

  Scenario Outline: Amend the completed and active field of the specific project
    Given a url http://localhost:4567/projects/:id where id = "<id>"
    When I instantiate the completed to "<completed>" and active field to "<active>"
    And passes that fields in a post request to the project
    Then the amend success code is returned
    And completed and active is modified
    Examples:
      | id | completed | active |
      | 1  | true      | true   |
      | 2  | true      | true   |

  Scenario Outline: delete a project with invalid id
    Given a url http://localhost:4567/projects/:id where id = "<id>"
    When I delete the project with invalid id
    Then an error code is returned
    Examples:
      | id |
      | 0  |
      | 10000|