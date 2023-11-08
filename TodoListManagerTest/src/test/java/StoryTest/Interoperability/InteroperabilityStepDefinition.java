package StoryTest.Interoperability;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;

public class InteroperabilityStepDefinition {

    private final String json = "application/json";
    private final String xml = "application/xml";
    private Response response;

    //  ---------- Feature: add category to to-do ---------- //

    @Given("I have an existing todo item and an existing category")
    public void i_have_an_existing_todo_item_and_an_existing_category() {
        String jsonString = "{" + "\"id\": \"4\"," + "\"title\": \"Photocopy Documents\"," + "\"doneStatus\": false," +
                "\"description\": \"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," + "}";

        RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .post("http://localhost:4567/todos");
    }
    @When("I create a relationship between the todo and the category in JSON")
    public void i_create_a_relationship_between_the_todo_and_the_category_in_json() {
        response = RestAssured.given()
                .header("Content-Type", json)
                .body("{ \"id\": \"1\"}")
                .post("http://localhost:4567/todos/4/categories");
    }
    @Then("I should see the category listed as a property of my todo item")
    public void i_should_see_the_category_listed_as_a_property_of_my_todo_item() {
        response = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/todos/4/categories");
        int statusCode = response.getStatusCode();
        String body = "{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        Assertions.assertEquals(body, response.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Given("I have a non existing todo item and an existing category")
    public void i_have_a_non_existing_todo_item_and_an_existing_category() {
        // Nothing to do here
    }

    @When("I create the todo item")
    public void i_create_the_todo_item() {
        String jsonString = "{" + "\"id\": \"4\"," + "\"title\": \"Photocopy Documents\"," + "\"doneStatus\": false," +
                "\"description\": \"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," + "}";

        RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .post("http://localhost:4567/todos");
    }

    @Then("I should be warned that the requested todo cannot be found")
    public void i_should_be_warned_that_the_requested_todo_cannot_be_found() {
        Assertions.assertEquals(404, response.getStatusCode());
        String error = "{\"errorMessages\":[\"Could not find parent thing for relationship todos/4/categories\"]}";
        Assertions.assertEquals(error, response.getBody().asString());
    }

    //  ---------- Feature: Get categories from a project ---------- //

    @Given("I have an existing project")
    public void i_have_an_existing_project() {
        String body = "{\"id\": \"1\"}";
        RestAssured.given()
                .body(body)
                .post("http://localhost:4567/projects/1/categories");
    }

    @When("I request all categories associated with the project in JSON")
    public void i_request_all_categories_associated_with_the_project_in_JSON() {
        response = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/projects/1/categories");
    }

    @Then("I should see all categories associated with the project in JSON")
    public void i_should_see_all_categories_associated_with_the_project_in_json() {
        Assertions.assertEquals(200, response.getStatusCode());
        String expect = "{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        Assertions.assertEquals(expect, response.getBody().asString());
    }

    @When("I request all categories associated with the project in XML")
    public void i_request_all_categories_associated_with_the_project_in_xml() {
        response = RestAssured.given()
                .header("Accept", xml)
                .get("http://localhost:4567/projects/1/categories");
    }

    @Then("I should see all categories associated with the project in XML")
    public void i_should_see_all_categories_associated_with_the_project_in_xml() {
        Assertions.assertEquals(200, response.getStatusCode());
        String expect = "<categories><category><description/><id>1</id><title>Office</title></category></categories>";
        Assertions.assertEquals(expect, response.getBody().asString());
    }

    @Given("I have a non existing project")
    public void i_have_a_non_existing_project() {
        // Nothing to do here
    }

    @When("I request all categories associated with the non existing project in JSON")
    public void i_request_all_categories_associated_with_the_non_existing_project_in_json() {
        response = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/projects/9/categories");
    }

    @Then("I should see no categories")
    public void i_should_see_no_categories() {
        Assertions.assertEquals(200, response.getStatusCode());
        String expect = "{\"categories\":[]}";
        Assertions.assertEquals(expect, response.getBody().asString());
    }

    //  ---------- Feature: delete category from project ---------- //

    @Given("I have an existing project and a category listed under it")
    public void i_have_an_existing_project_and_a_category_listed_under_it() {
        // Project 1 already created when initialized
        RestAssured.given()
                .header("Content-Type", json)
                .body("{\"id\":\"1\"}")
                .post("http://localhost:4567/projects/1/categories");
    }

    @When("I delete the relationship between the project and the category")
    public void i_delete_the_relationship_between_the_project_and_the_category() {
        RestAssured.given()
                .header("Content-Type", json)
                .delete("http://localhost:4567/projects/1/categories/1");
    }

    @Then("I should no longer see the deleted category listed under the project")
    public void i_should_no_longer_see_the_deleted_category_listed_under_the_project() {
        String expect = "{\"projects\":[{\"id\":\"1\",\"title\":\"Office Work\",\"completed\":\"false\"," +
                "\"active\":\"false\",\"description\":\"\",\"tasks\":[{\"id\":\"1\"},{\"id\":\"2\"}]}]}";
        response = RestAssured.given()
                .header("Content-Type", json)
                .get("http://localhost:4567/projects/1");
        Assertions.assertEquals(200, response.getStatusCode());
        JSONAssert.assertEquals(expect, response.getBody().asString(), false);
    }

    @When("I delete the project")
    public void i_delete_the_project() {
        RestAssured.given()
                .delete("http://localhost:4567/projects/1");
    }

    @And("I create a new project with the same properties except for the category I want to delete")
    public void i_create_a_new_project_with_the_same_properties_except_for_the_category_i_want_to_delete() {
        String proj = "{\n" +
                "    \"title\": \"Office Work\",\n" +
                "    \"completed\": false,\n" +
                "    \"active\": false,\n" +
                "    \"description\": \"\"\n" +
                "}";
        RestAssured.given()
                .header("Content-Type", json)
                .body(proj)
                .post("http://localhost:4567/projects");

        String task1 = "{\"id\":\"1\"}";
        String task2 = "{\"id\":\"2\"}";
        RestAssured.given()
                .header("Content-Type", json)
                .body(task1)
                .post("http://localhost:4567/projects/2/tasks");
        RestAssured.given()
                .header("Content-Type", json)
                .body(task2)
                .post("http://localhost:4567/projects/2/tasks");
    }

    @Then("I should no longer see the deleted category listed under the new project")
    public void i_should_no_longer_see_the_deleted_category_listed_under_the_new_project() {
        String expect = "{\"projects\":[{\"id\":\"2\",\"title\":\"Office Work\",\"completed\":\"false\"," +
                "\"active\":\"false\",\"description\":\"\",\"tasks\":[{\"id\":\"1\"},{\"id\":\"2\"}]}]}";
        response = RestAssured.given()
                .header("Content-Type", json)
                .get("http://localhost:4567/projects/2");
        Assertions.assertEquals(200, response.getStatusCode());
        JSONAssert.assertEquals(expect, response.getBody().asString(), false);
    }

    @When("I delete the relationship between the non existing project and the category")
    public void i_delete_the_relationship_between_the_non_existing_project_and_the_category() {
        response = RestAssured.given()
                .header("Content-Type", json)
                .delete("http://localhost:4567/projects/4/categories/1");
    }

    @Then("I should be warned that the requested project cannot be found")
    public void i_should_be_warned_that_the_requested_project_cannot_be_found() {
        Assertions.assertEquals(404, response.getStatusCode());
        String error = "{\"errorMessages\":[\"Could not find any instances with projects/4/categories/1\"]}";
        Assertions.assertEquals(error, response.getBody().asString());
    }

    //  ---------- Feature: Get todos from a category ---------- //

    @Given("I have an existing category")
    public void i_have_an_existing_category() {
        String body = "{\"id\": \"1\"}";
        RestAssured.given()
                .body(body)
                .post("http://localhost:4567/categories/1/todos");
    }

    @When("I request all todos associated with the category in JSON")
    public void i_request_all_todos_associated_with_the_category_in_json() {
        response = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/categories/1/todos");
    }

    @Then("I should see all todos associated with the category in JSON")
    public void i_should_see_all_todos_associated_with_the_category_in_json() {
        Assertions.assertEquals(200, response.getStatusCode());
        String expect = "{\"todos\":[{\"id\":\"1\",\"title\":\"scan paperwork\",\"doneStatus\":\"false\"," +
                "\"description\":\"\",\"tasksof\":[{\"id\":\"1\"}],\"categories\":[{\"id\":\"1\"}]}]}";
        Assertions.assertEquals(expect, response.getBody().asString());
    }

    @When("I request all todos associated with the category in XML")
    public void i_request_all_todos_associated_with_the_category_in_xml() {
        response = RestAssured.given()
                .header("Accept", xml)
                .get("http://localhost:4567/categories/1/todos");
    }

    @Then("I should see all todos associated with the category in XML")
    public void i_should_see_all_todos_associated_with_the_category_in_xml() {
        Assertions.assertEquals(200, response.getStatusCode());
        String expect = "<todos><todo><doneStatus>false</doneStatus><description/>" +
                "<tasksof><id>1</id></tasksof><id>1</id>" +
                "<categories><id>1</id></categories><title>scan paperwork</title></todo></todos>";
        Assertions.assertEquals(expect, response.getBody().asString());
    }

    @Given("I have a non existing category")
    public void i_have_a_non_existing_category() {
        // Nothing to do here
    }

    @When("I request all todos associated with the non existing category in JSON")
    public void i_request_all_todos_associated_with_the_non_existing_category_in_json() {
        response = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/categories/9/todos");
    }

    @Then("I should see no todos")
    public void i_should_see_no_todos() {
        Assertions.assertEquals(200, response.getStatusCode());
        String expect = "{\"todos\":[]}";
        Assertions.assertEquals(expect, response.getBody().asString());
    }
}
