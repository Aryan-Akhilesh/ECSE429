@tag
Feature: Get project information
  As a user, I want to get a project to know about its condition and information.

  Background:
    Given dummy Project to be got


  Scenario Outline: get the project using Json
    Given target url http://localhost:4567/projects/:id with id = "<dummyid>"
    When get a project using get request
    Then the request project is returned
    And success code is returned
    Examples:
      | dummyid |
      |2        |


  Scenario Outline: get the project using Xml
    Given target url http://localhost:4567/projects/:id with id = "<dummyid>"
    When get a project using get request specific in xml
    Then the request project of xml form is returned
    And success code is returned
    Examples:
      | dummyid |
      | 2       |

  Scenario Outline: get the project with invalid id
    Given wrong target url http://localhost:4567/projects/:id with id = "<wrongid>"
    When get a project using get request with wrong url
    Then the error code is returned
    And specific errorMessage is displayed
    Examples:
      | wrongid |
      | 100 |
      | 0 |