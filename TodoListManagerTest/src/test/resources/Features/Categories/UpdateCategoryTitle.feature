Feature: Update a category title
  PUT \categories\:id with the appropriate request body

  Scenario Outline: Normal flow for updating a category title
    Given I have an existing category with field title "<prevTitle>"
    When I update the existing title to a new title "<newTitle>"
    Then I should see the title of the category changed to a new title "<newTitle>"
    Examples:
      | prevTitle | newTitle |
      |  Train    |   Plane  |
      |  Plane    |   Train  |

  Scenario Outline: Alternate flow for updating a category title
    Given I have an existing category with field title "<prevTitle>"
    When I use Post request to update my existing category title to a new title "<newTitle>"
    Then I should see the title of the category changed to a new title "<newTitle>"
    Examples:
      | prevTitle | newTitle |
      |  Train    |   Plane  |
      |  Plane    |   Train  |

  Scenario Outline: Error flow for updating a category title
    Given I have an existing category with field title "<prevTitle>"
    When I update the existing title to a new title "<newTitle>" with incorrect Id <invalidCategoryId>
    Then I should be warned that id <invalidCategoryId> is invalid
    Examples:
      | prevTitle | newTitle | invalidCategoryId |
      |  Train    |   Plane  |         300       |
      |  Plane    |   Train  |          0        |
