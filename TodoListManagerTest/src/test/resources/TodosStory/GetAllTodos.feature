Feature: Get all todos

  Scenario Outline: Normal Flow for getting all todos
    Given I have existing todos
    When I get all todos in JSON
    Then I should see all todos in JSON
    Examples:
      |  |

  Scenario Outline: Alternate flow for getting all todos
    Given I have existing todos
    When I get all todos in XML
    Then I should see all todos in XML
    Examples:
      |  |

  Scenario Outline: Error flow for getting all todos
    Given I have existing todos
    When I get todo with invalid id <id>
    Then I should not see any todo
    Examples:
      | id |
      | 54 |
