Feature: Get all categories
  GET to \categories

  Scenario Outline: Normal flow for getting all categories
    Given I have an existing category database
    When I request all category in JSON using valid URL "<URL>"
    Then I should see all existing categories in JSON
    Examples:
      |               URL                |
      | http://localhost:4567/categories |

  Scenario Outline: Alternate flow for getting all categories
     Given I have an existing category database
     When I request all category in xml using valid URL "<URL>"
     Then I should see all existing categories in XML
    Examples:
      |               URL                |
      | http://localhost:4567/categories |

   Scenario Outline: Error flow for getting all categories
     Given I have an existing category database
     When I request all categories using invalid url "<invalidURL>"
     Then I should be warned that the url is not found
     Examples:
       |           invalidURL             |
       | http://localhost:4567/cat**egies |


