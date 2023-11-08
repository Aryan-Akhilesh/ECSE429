@tag
Feature: Amend project fields
  As a user, I want to be able to amend the title field of a project

  Background:
    Given dummy Project to be amended


  Scenario: amend project fields using POST
    Given http://localhost:4567/projects/:id
    When I instantiate the title field
    And passes that field in a post request to the Dummy project
    Then amend success code is returned
    And title is modified
    And I delete dummy project


  Scenario: amend project fields using PUT
    Given http://localhost:4567/projects/:id
    When I instantiate the title field
    And passes that field in a put request to the Dummy project
    Then amend success code is returned
    And title is modified
    And I delete dummy project

  Scenario: amend invalid project fields using PUT
    Given http://localhost:4567/projects/:id
    When I instantiate the topic field
    And passes that field in a post request to the Dummy project
    Then amend error code is returned
    And amend error message is displayed
    And I delete dummy todo