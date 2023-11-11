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
    private String jsonString;
    private String xmlString;

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

    // --------------- Feature: Add a new todos -------------//

    @Given("I have a todo that does not exist and wish to add one")
    public void i_have_a_todo_that_does_not_exist_and_wish_to_add_one() {
    }

    @When("I create a todo with title {string}, description {string}, & doneStatus {string}")
    public void i_create_a_todo_with_title_description_done_status(String title, String description, String doneStatus) {
        jsonString = "{" + "\"title\": \" " + title + "\"," + "\"doneStatus\": "+ doneStatus +"," +
                "\"description\": \""+description+"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," +
                "\"categories\": [" + "{" + "\"id\": \"1\"" + "}" + "]" + "}";
    }

    @Then("I should see the todo listed in the todos")
    public void i_should_see_the_todo_listed_in_the_todos() {
         res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(201,statusCode);
    }

    @When("I create a todo with just the title {string}")
    public void i_create_a_todo_with_just_the_title(String title) {
         jsonString = "{" + "\"title\": \""+title+"\"" + "}";
    }

    @When("I create a todo with an invalid field {string}")
    public void i_create_a_todo_with_an_invalid_field(String field) {
        jsonString = "{" + "\""+field+"\": \"Documents\"" + "}";
    }

    @Then("I should not see the todo listed")
    public void i_should_not_see_the_todo_listed() {
        res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(400,statusCode);
    }

    //------------ Feature: Delete todos -----------//

    @When("I delete the todo with the valid id {int}")
    public void i_delete_the_todo_with_the_valid_id(Integer todoID) {
        res = RestAssured.delete(" http://localhost:4567/todos/" + todoID);
    }

    @Then("I should not see the todo listed under todos anymore")
    public void i_should_not_see_the_todo_listed_under_todos_anymore() {
        int statusCode = res.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

    @When("I post todo with valid id {int} but with the doneStatus {string} as completed")
    public void i_post_todo_with_valid_id_but_with_the_done_status_as_completed(Integer todoID, String doneStatus) {
        jsonString =  "{" + "\"id\": "+todoID+ "," + "\"doneStatus\": "+ doneStatus + "}";
        res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .post("http://localhost:4567/todos/" + todoID);
    }

    @Then("I should see the todo but with completed status in todos")
    public void i_should_see_the_todo_but_with_completed_status_in_todos() {
        int statusCode = res.getStatusCode();
        Assertions.assertEquals(200,statusCode);
        System.out.println(res.getBody().asString());
    }

    @When("I delete the todo with the invalid id {int}")
    public void i_delete_the_todo_with_the_invalid_id(Integer todoID) {
        res = RestAssured.delete(" http://localhost:4567/todos/" + todoID);
    }

    @Then("The todo is still listed under todos")
    public void the_todo_is_still_listed_under_todos() {
        int statusCode = res.getStatusCode();
        Assertions.assertEquals(404,statusCode);
    }

    //--------------Feature: Amend Description --------------//

    @When("I PUT a todo with valid id {int} and different description {string}")
    public void i_put_a_todo_with_valid_id_and_different_description(Integer todoID, String description) {
        jsonString =  "{" + "\"id\":"+ todoID +"," +"\"title\": \"get more paperwork\"," + "\"doneStatus\": false," +
                "\"description\": \""+ description +"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," +
                "\"categories\": [" + "{" + "\"id\": \"1\"" + "}" + "]" + "}";

        res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .put("http://localhost:4567/todos/" + todoID);
    }

    @Then("I should see that the description has changed for that todo")
    public void i_should_see_that_the_description_has_changed_for_that_todo() {
        int statusCode = res.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

    @When("I POST a todo with valid id {int} and different description {string}")
    public void i_post_a_todo_with_valid_id_and_different_description(Integer todoID, String description) {
        jsonString =  "{" + "\"id\": "+todoID+ "," + "\"description\": \""+ description + "\"}";
        res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .post("http://localhost:4567/todos/" + todoID);
    }

    @When("I PUT a todo with invalid id {int} and different description {string}")
    public void i_put_a_todo_with_invalid_id_and_different_description(Integer todoID, String description) {
        jsonString =  "{" + "\"id\": "+todoID+ "," + "\"description\": \""+ description + "\"}";
        res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .put("http://localhost:4567/todos/" + todoID);
    }

    @Then("I should see no change to the description for that todo")
    public void i_should_see_no_change_to_the_description_for_that_todo() {
        int statusCode = res.getStatusCode();
        Assertions.assertEquals(404,statusCode);
    }



}
