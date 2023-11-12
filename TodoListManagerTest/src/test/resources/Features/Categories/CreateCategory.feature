Feature: Create a new category
  POST to /categories with desired request body

  Scenario Outline: Normal flow for creating a new category
    Given I do not have a custom category
    When I create a new category with title "<newTitle>" and description field "<newDescription>"
    Then I should see a new category with new title "<newTitle>" and description "<newDescription>"
    Examples:
      | newTitle | newDescription |
      |  Train   |   Bullet       |
      |  Plane   |   Jet          |

  Scenario Outline: Alternate flow for creating a new category
    Given I do not have a custom category
    When I create a new category with title "<newTitle>" only
    Then I should see a new category with title "<newTitle>" only
    Examples:
      |   newTitle  |
      | University  |
      | High School |

  Scenario Outline: Error flow for creating a new category
    Given I do not have a custom category
    When I create a new category with an id field <categoryId>
    Then I should be warned that id is not allowed
    Examples:
      | categoryId |
      |   3        |

