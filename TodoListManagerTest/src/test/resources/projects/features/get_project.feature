@tag
Feature: Get project information
  As a user, I want to get a project to know about its condition and information.

  Background:
    Given dummy Project to be got


  Scenario: get the project using Json
    Given target url http://localhost:4567/projects/:id
    When get a project using get request
    Then the request project is returned
    And success code is returned
    And I delete the target dummy project


  Scenario: get the project using Xml
    Given target url http://localhost:4567/projects/:id
    When get a project using get request specific in xml
    Then the request project of xml form is returned
    And success code is returned
    And I delete the target dummy project

  Scenario: get the project with invalid id
    Given wrong target url http://localhost:4567/projects/:id
    When get a project using get request with wrong url
    Then the error code is returned
    And specific errorMessage is displayed
    And I delete the target dummy project