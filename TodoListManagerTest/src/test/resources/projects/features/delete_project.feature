@tag
  Feature: Delete projects
    As a user, I want to close a project since it is already finished.

    Background:
      Given a dummy Project to be amended


    Scenario: delete a project with specific project id
      Given a url http://localhost:4567/projects/:id
      When I delete the project with id
      Then a success code should be returned


    Scenario: Amend the completed and active field of the specific project
      Given a url http://localhost:4567/projects/:id
      When I instantiate the completed to true and active field to false
      And passes that fields in a post request to the Dummy project
      Then the amend success code is returned
      And completed and active is modified
      And I delete the dummy project


    Scenario: delete a project with invalid id
      Given wrong url http://localhost:4567/projects/:id
      When I delete the project with invalid id
      Then an error code is returned
      And I delete the dummy project
