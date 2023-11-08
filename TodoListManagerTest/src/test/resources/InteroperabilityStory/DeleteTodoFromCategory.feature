Feature: Delete a todo from a category
  DELETE to \categories\:id\todos\:id

  Scenario: Normal flow for deleting a todo from a category
    Given I have an existing category and a todo listed under it
    When I delete the relationship between the category and the todo
    Then I should no longer see the deleted todo listed under the category

  Scenario: Alternate flow for deleting a todo from a category
    Given I have an existing category and a todo listed under it
    When I delete the category
    And I create a new category with the same properties except for the todo I want to delete
    Then I should no longer see the deleted todo listed under the new category

  Scenario: Error flow for deleting a todo from a category
    Given I have a non existing category
    When I delete the relationship between the non existing category and the todo
    Then I should be warned that the requested category cannot be found