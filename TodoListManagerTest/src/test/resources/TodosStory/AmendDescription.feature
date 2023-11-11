Feature: Amend the description of a todo

  Scenario Outline: Normal flow for amending the description of a todo
    Given I have an existing todo
    When I PUT a todo with valid id <id> and different description <description>
    Then I should see that the description has changed for that todo
    Examples:
      | id | description       |
      |1   | "Add more documents"|

  Scenario Outline: Alternate flow for amending the description of a todo
    Given I have an existing todo
    When I POST a todo with valid id <id> and different description <description>
    Then I should see that the description has changed for that todo
    Examples:
      | id | description       |
      | 1  | "Add more documents"|

  Scenario Outline: Error flow for amending the description of a todo
    Given I have a non-existing todo
    When I PUT a todo with invalid id <id> and different description <description>
    Then I should see no change to the description for that todo
    Examples:
      | id | description       |
      | 56 | "Add more documents"|

