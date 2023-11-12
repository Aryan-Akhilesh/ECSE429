Feature: Get a specific todo


  Scenario Outline: Normal flow for getting a specific todo
    Given I have an existing todo
    When I request the todo with a specific id <todoID> in JSON
    Then I should see the todo in JSON
    Examples:
      | todoID |
      |   1    |

  Scenario Outline: Alternate flow for getting a specific todo
    Given I have an existing todo
    When I request the todo with a specific id <todoID> in XML
    Then I should see the todo in XML
    Examples:
      | todoID |
      |   1    |

  Scenario Outline: Error flow for getting a specific todo
    Given I have a non-existing todo
    When I request the todo with an invalid id <todoID> in JSON
    Then I should see no todo with the id <todoID>
    Examples:
      | todoID |
      |    86  |
      |999     |
