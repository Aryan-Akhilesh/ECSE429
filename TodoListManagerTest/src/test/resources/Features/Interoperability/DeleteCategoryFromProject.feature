Feature: Delete a category from a project item
  DELETE to \projects\:id\categories\:id

  Scenario Outline: Normal flow for deleting a category from a project item
    Given I have an existing project <projectId> and a category <categoryId> listed under it
    When I delete the relationship between the project <projectId> and the category <categoryId>
    Then I should no longer see the deleted category listed under the project <projectId>
    Examples:
      | projectId | categoryId |
      |    1      |      1     |
      |    2      |      1     |

  Scenario Outline: Alternate flow for deleting a category from a project item
    Given I have an existing project <projectId> and a category <categoryId> listed under it
    When I delete the project <projectId>
    And I create a new project with the same properties except for the category I want to delete
    Then I should no longer see the deleted category listed under the new project
    Examples:
      | projectId | categoryId |
      |    1      |      1     |
      |    2      |      1     |

  Scenario Outline: Error flow for deleting a category from a project item
    Given I have a non existing project
    When I delete the relationship between the non existing project <projectId> and the category <categoryId>
    Then I should be warned that the requested project <projectId> and category <categoryId> cannot be found
    Examples:
      | projectId | categoryId |
      |    5      |      1     |
      |    7      |      1     |