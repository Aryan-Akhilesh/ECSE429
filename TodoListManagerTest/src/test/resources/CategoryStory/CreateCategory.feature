Feature: Create a new category
  POST to /categories with desired request body

  Scenario Outline: Normal flow for creating a new category
    Given I do not have a custom category
    When I create a new category with title and description field
    Then I should see a new category <categoryId> with title and description
    Examples:
      | categoryId |
      |   3        |

  Scenario Outline: Alternate flow for creating a new category
    Given I do not have a custom category
    When I create a new category with title only
    Then I should see a new category <categoryId> with title only
    Examples:
      | categoryId |
      |   3        |

  Scenario Outline: Error flow for creating a new category
    Given I do not have a custom category
    When I create a new category with an id field <categoryId>
    Then I should be warned that id is not allowed
    Examples:
      | categoryId |
      |   3        |

