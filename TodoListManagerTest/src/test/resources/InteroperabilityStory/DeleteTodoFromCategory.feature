Feature: Delete a todo from a category
  DELETE to \categories\:id\todos\:id

  Scenario Outline: Normal flow for deleting a todo from a category
    Given I have an existing category <categoryId> and a todo <todoId> listed under it
    When I delete the relationship between the category <categoryId> and the todo <todoId>
    Then I should no longer see the deleted todo listed under the category <categoryId>
    Examples:
      | categoryId | todoId |
      |    1       |    1   |
      |    2       |    1   |

  Scenario Outline: Alternate flow for deleting a todo from a category
    Given I have an existing category <categoryId> and a todo <todoId> listed under it
    When I delete the category <categoryId>
    And I create a new category with the same properties except for the todo I want to delete
    Then I should no longer see the deleted todo listed under the new category
    Examples:
      | categoryId | todoId |
      |    1       |    1   |
      |    2       |    1   |

  Scenario Outline: Error flow for deleting a todo from a category
    Given I have a non existing category
    When I delete the relationship between the non existing category <categoryId> and the todo <todoId>
    Then I should be warned that the requested category <categoryId> and todo <todoId> cannot be found
    Examples:
      | categoryId | todoId |
      |    6       |    1   |
      |    8       |    1   |