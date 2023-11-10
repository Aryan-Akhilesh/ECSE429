Feature: Add a new todo

  Scenario Outline: Normal flow for adding a todo
    Given I have a todo that does not exist and wish to add one
    When I create a todo with title <title>, description <description>, & doneStatus <doneStatus>
    Then I should see the todo listed in the todos
    Examples:
      |title         | description                  | doneStatus |
      |Send documents| send the documents to the CEO|  false     |

  Scenario Outline: Alternate flow for adding a todo
    Given I have a todo that does not exist and wish to add one
    When I create a todo with just the title <title>
    Then I should see the todo listed in the todos
    Examples:
      | title         |
      |Shred documents|

  Scenario Outline: Error flow for adding a todo
    Given I have a todo that does not exist and wish to add one
    When I create a todo with an invalid field <field>
    Then I should not see the todo listed
    Examples:
      | field |
      |class  |
