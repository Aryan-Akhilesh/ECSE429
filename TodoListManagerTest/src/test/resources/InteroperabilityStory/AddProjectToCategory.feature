Feature: Get all categories from a project
  GET to \projects\:id\categories

  Scenario: Normal flow for getting all categories from a project
    Given I have an existing project
    When I request all categories associated with the project in JSON
    Then I should see all categories associated with the project in JSON

  Scenario: Alternate flow for getting all categories from a project
    Given I have an existing project
    When I request all categories associated with the project in XML
    Then I should see all categories associated with the project in XML

  Scenario: Error flow for getting all categories from a project
    Given I have a non existing project
    When I request all categories associated with the non existing project in JSON
    Then I should see no categories