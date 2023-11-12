Feature: Get a specific category
  GET to \categories\:id

 Scenario Outline: Normal flow for get a specific category
   Given I have an existing category database
   When I request a category with id <categoryId> in JSON
   Then I should see an existing category with id <categoryId> in JSON
   Examples:
     | categoryId |
     |      1     |

  Scenario Outline: Alternate flow for get a specific category
    Given I have an existing category database
    When I request a category with id <categoryId> in XML
    Then I should see an existing category with id <categoryId> in XML
    Examples:
      | categoryId |
      |      1     |

  Scenario Outline: Error flow for get a specific category
    Given I have an existing category database
    When I request a category with an invalid id <invalidCategoryId> in JSON
    Then I should be warned that instance does not exist in category with id <invalidCategoryId>
    Examples:
      | invalidCategoryId |
      |   300             |
