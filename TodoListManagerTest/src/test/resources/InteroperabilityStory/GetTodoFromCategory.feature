Feature: Get all todo items from a category
  GET to \categories\:id\todos

  Scenario: Normal flow for getting all todos from a category
    Given I have an existing category
    When I request all todos associated with the category in JSON
    Then I should see all todos associated with the category in JSON

  Scenario: Alternate flow for getting all todos from a category
    Given I have an existing category
    When I request all todos associated with the category in XML
    Then I should see all todos associated with the category in XML

  Scenario: Error flow for getting all todos from a category
    Given I have a non existing category
    When I request all todos associated with the non existing category in JSON
    Then I should see no todos