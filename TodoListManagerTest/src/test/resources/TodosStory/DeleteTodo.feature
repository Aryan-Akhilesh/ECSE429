Feature: Delete a todo

  Scenario Outline: Normal flow for deleting a todo
    Given I have an existing todo
    When I delete the todo with the valid id <id>
    Then I should not see the todo listed under todos anymore
    Examples:
      | id |
      | 1  |

  Scenario Outline: Alternate flow for deleting a todo
    Given I have an existing todo
    When I post todo with valid id <id> but with the doneStatus <doneStatus> as completed
    Then I should see the todo but with completed status in todos
    Examples:
      | id | doneStatus |
      | 1  | "true"  |

  Scenario Outline: Error flow for deleting a todo
    Given I have a non-existing todo
    When I delete the todo with the invalid id <id>
    Then The todo is still listed under todos
    Examples:
      | id |
      |55  |

