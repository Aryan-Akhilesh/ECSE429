package StoryTest.Todos;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;

public class TodosStepDefinition {

    private final String json = "application/json";
    private final String xml = "application/xml";
    private Response res;

    // ----------- Feature: Get specific todos -------------//

    @Given("I have an existing todo")
    public void i_have_an_existing_todo() {
        // 2 todos already exist
    }

    @When("I request the todo with a specific id {int} in JSON")
    public void i_request_the_todo_with_a_specific_id_in_json(Integer todoID) {
        res = RestAssured.given().header("Accept",json).get("http://localhost:4567/todos/" + todoID);
    }

    @Then("I should see the todo in JSON")
    public void i_should_see_the_todo_in_json() {
        int statusCode = res.getStatusCode();
        String body = "{\"todos\":[{\"id\":\"1\",\"title\":\"scan paperwork\",\"doneStatus\":\"false\",\"description\":\"\",\"categories\":[{\"id\":\"1\"}],\"tasksof\":[{\"id\":\"1\"}]}]}";
        JSONAssert.assertEquals(body,res.getBody().asString(), false);
        Assertions.assertEquals(200,statusCode);
    }

    @When("I request the todo with a specific id {int} in XML")
    public void i_request_the_todo_with_a_specific_id_in_xml(Integer todoID) {
        res = RestAssured.given().header("Accept",xml).get("http://localhost:4567/todos/" + todoID);
    }

    @Then("I should see the todo in XML")
    public void i_should_see_the_todo_in_xml() {
        int statusCode = res.getStatusCode();
        String body = "<todos><todo><doneStatus>false</doneStatus><description/><tasksof><id>1</id></tasksof><id>1</id><categories><id>1</id></categories><title>scan paperwork</title></todo></todos>";
        Assertions.assertEquals(body,res.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Given("I have a non-existing todo")
    public void i_have_a_non_existing_todo() {
        //nothing to do here
    }

    @When("I request the todo with an invalid id {int} in JSON")
    public void i_request_the_todo_with_an_invalid_id_in_json(Integer todoID) {
        res = RestAssured.given().header("Accept",json).get("http://localhost:4567/todos/" + todoID);
    }

    @Then("I should see no todo with the id {int}")
    public void i_should_see_no_todo(Integer todoID) {
        int statusCode = res.getStatusCode();
        String body = "{\"errorMessages\":[\"Could not find an instance with todos/" + todoID + "\"]}";
        Assertions.assertEquals(body,res.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

}
