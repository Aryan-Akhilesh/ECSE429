Feature: Delete a category from a project item
  DELETE to \projects\:id\categories\:id

  Scenario: Normal flow for deleting a category from a project item
    Given I have an existing project and a category listed under it
    When I delete the relationship between the project and the category
    Then I should no longer see the deleted category listed under the project

  Scenario: Alternate flow for deleting a category from a project item
    Given I have an existing project and a category listed under it
    When I delete the project
    And I create a new project with the same properties except for the category I want to delete
    Then I should no longer see the deleted category listed under the new project

  Scenario: Error flow for deleting a category from a project item
    Given I have a non existing project
    When I delete the relationship between the non existing project and the category
    Then I should be warned that the requested project cannot be found