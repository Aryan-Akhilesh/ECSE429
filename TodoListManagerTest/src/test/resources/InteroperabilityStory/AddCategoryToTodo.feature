Feature: Add a category to a todo item
  POST to \todo\:id\categories with specific category in the request body

  Scenario Outline: Normal flow for adding a category to a todo item
    Given I have an existing todo <todoId> and an existing category <categoryId>
    When I create a relationship between the todo <todoId> and the category <categoryId> in JSON
    Then I should see the category listed as a property of my todo <todoId>
    Examples:
      | todoId | categoryId |
      |    1   |      1     |
      |    2   |      1     |

  Scenario Outline: Alternate flow for adding a category to a todo item
    Given I have a non existing todo and an existing category
    When I create the todo <todoId>
    And I create a relationship between the todo <todoId> and the category <categoryId> in JSON
    Then I should see the category listed as a property of my todo <todoId>
    Examples:
      | todoId | categoryId |
      |    6   |      1     |
      |    8   |      1     |

  Scenario Outline: Error flow for adding a category to a todo item
    Given I have a non existing todo and an existing category
    When I create a relationship between the todo <todoId> and the category <categoryId> in JSON
    Then I should be warned that the requested todo <todoId> cannot be found
    Examples:
      | todoId | categoryId |
      |    6   |      1     |
      |    8   |      1     |