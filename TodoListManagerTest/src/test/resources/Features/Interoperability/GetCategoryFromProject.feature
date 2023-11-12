Feature: Get all categories from a project
  GET to \projects\:id\categories

  Scenario Outline: Normal flow for getting all categories from a project
    Given I have an existing project
    When I request all categories associated with the project <projectId> in JSON
    Then I should see all categories associated with the project <projectId> in JSON
    Examples:
      | projectId |
      |    1      |

  Scenario Outline: Alternate flow for getting all categories from a project
    Given I have an existing project
    When I request all categories associated with the project <projectId> in XML
    Then I should see all categories associated with the project <projectId> in XML
    Examples:
      | projectId |
      |    1      |

  Scenario Outline: Error flow for getting all categories from a project
    Given I have a non existing project
    When I request all categories associated with the non existing project <projectId> in JSON
    Then I should see no categories
    Examples:
      | projectId |
      |    5      |
      |    6      |