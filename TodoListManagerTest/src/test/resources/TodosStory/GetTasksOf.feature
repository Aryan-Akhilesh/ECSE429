Feature: Get tasksOf todos

  Scenario Outline: Normal Flow for getting tasksOf a todo
    Given I have existing todo
    When I get the taskOf the todo with the valid id <id> in JSON
    Then I should see taskOf the todo in JSON
    Examples:
      | id |
      |1   |
      |2   |


  Scenario Outline: Alternate flow for getting tasksOf a todo
    Given I have existing todos
    When I get the taskOf the todo with the valid id <id> in XML
    Then I should see taskOf the todo in XML
    Examples:
      | id |
      |1   |
      |2   |


  Scenario Outline: Error flow for getting all todos
    Given I have existing todos
    When I get the taskOf the todo with the invalid id <id>
    Then I should not see any taskOf
    Examples:
      | id |
      | 999 |
      |555  |
