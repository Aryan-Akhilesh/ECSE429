Feature: Get all todo items from a category
  GET to \categories\:id\todos

  Scenario Outline: Normal flow for getting all todos from a category
    Given I have an existing category <categoryId>
    When I request all todos associated with the category <categoryId> in JSON
    Then I should see all todos associated with the category in JSON
    Examples:
      | categoryId |
      |    1       |
      |    2       |

  Scenario Outline: Alternate flow for getting all todos from a category
    Given I have an existing category <categoryId>
    When I request all todos associated with the category <categoryId> in XML
    Then I should see all todos associated with the category in XML
    Examples:
      | categoryId |
      |    1       |
      |    2       |

  Scenario Outline: Error flow for getting all todos from a category
    Given I have a non existing category
    When I request all todos associated with the non existing category <categoryId> in JSON
    Then I should see no todos
    Examples:
      | categoryId |
      |    4       |
      |    5       |