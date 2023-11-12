Feature: Replace an existing category
  PUT \category\:id with desired appropriate request body

  Scenario Outline: Normal flow for replacing an existing category
    Given I have an existing category with title "<prevTitle>" and description "<prevDescription>"
    When I replace the title and description field of an existing category to a new title "<newTitle>" and description "<newDescription>"
    Then I should see the category field replaced to new title "<newTitle>" and new description "<newDescription>"
    Examples:
      | prevTitle | newTitle | prevDescription | newDescription |
      |  Train    |   Plane  |      Armored    |     Civilian   |
      |  Plane    |   Train  |      Civilian   |     Armored    |

  Scenario Outline: Alternate flow for replacing an existing category
    Given I have an existing category with title "<prevTitle>" and description "<prevDescription>"
    When I delete the current existing category
    And I create a new category with title "<newTitle>" and description "<newDescription>"
    Then I should see the title and description of the category replaced to a new title "<newTitle>" and description "<newDescription>" after deletion
    Examples:
      | prevTitle | newTitle | prevDescription | newDescription |
      |  Train    |   Plane  |      Armored    |     Civilian   |
      |  Plane    |   Train  |      Civilian   |     Armored    |

  Scenario Outline: Error flow for replacing an existing category
    Given I have an existing category with title "<prevTitle>" and description "<prevDescription>"
    When I replace the category with a new title "<newTitle>" and new description "<newDescription>" with an invalid id <invalidCategoryId>
    Then I should be warned that id <invalidCategoryId> is invalid
    Examples:
      | prevTitle | newTitle | prevDescription | newDescription | invalidCategoryId |
      |  Train    |   Plane  |      Armored    |     Civilian   |        0          |
      |  Plane    |   Train  |      Civilian   |     Armored    |       300         |

