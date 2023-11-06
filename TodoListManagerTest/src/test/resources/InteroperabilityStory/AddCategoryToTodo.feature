Feature: Add a category to a todo item
  POST to \todo\:id\categories with specific category in the request body

  Scenario: Normal flow for adding a category to a todo item
    Given I have an existing todo item and an existing category
    When I create a relationship between the todo and the category in JSON format
    Then I should see the category listed as a property of my todo item

  Scenario: Alternate flow for adding a category to a todo item
    Given I have a non existing todo item and an existing category
    When I create the todo item
    And I create a relationship between the todo and the category in JSON format
    Then I should see the category listed as a property of my todo item

  Scenario: Error flow for adding a category to a todo item
    Given I have a non existing todo item and an existing category
    When I create a relationship between the todo and the category in JSON format
    Then I should be warned that the requested todo cannot be found