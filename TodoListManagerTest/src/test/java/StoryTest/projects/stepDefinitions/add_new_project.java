package StoryTest.projects.stepDefinitions;
import io.cucumber.java.en.And;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class add_new_project {

    private String url = null;
    private JSONObject body = null;
    Response response = null;

    private String title = "";
    private String description = "";
    private boolean completed = false;
    private boolean active = false;


    @Given("the URL https:\\/\\/localhost:4567\\/projects")
    public void theURLHttpsLocalhostProjects() {
        url = "http://localhost:4567/projects";
    }

    @When("user creates a project with {string}, {string}, {string} and {string}")
    public void userCreatesAProjectWithAnd(String arg0, String arg1, String arg2, String arg3) {
        title = arg0;
        completed = Boolean.parseBoolean(arg1);
        active = Boolean.parseBoolean(arg2);
        description = arg3;
        body = new JSONObject();
        body.put("title", title);
        body.put("completed", completed);
        body.put("active", active);
        body.put("description", description);
    }

    @And("passes that project in a post request")
    public void passesThatProjectInAPostRequest() {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body(body.toString()).post(url);
    }

    @Then("success error code is returned")
    public void successErrorCodeIsReturned() {
        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201, but it's not");

    }

    @And("object body is returned")
    public void objectBodyIsReturned() {
        SoftAssert softAssert = new SoftAssert();

        String newTitle = response.jsonPath().getString("title");
        softAssert.assertEquals(newTitle, title);
        boolean newCompleted = Boolean.parseBoolean(response.jsonPath().getString("completed"));
        softAssert.assertEquals(newCompleted, completed);
        boolean newActive = Boolean.parseBoolean(response.jsonPath().getString("active"));
        softAssert.assertEquals(newActive, active);
        String newDescription = response.jsonPath().getString("description");
        softAssert.assertEquals(newDescription, description);

        System.out.println("=======> Printing Added Project: ");
        System.out.println("Title: " + title);
        System.out.println("Completed: " + completed);
        System.out.println("Active: " + active);
        System.out.println("Description: " + description);
        
    }

    @And("todo list state is restored by deleting newly created project")
    public void todoListStateIsRestoredByDeletingNewlyCreatedProject() {
        String newId = response.jsonPath().getString("id");
        RestAssured.given().delete("http://localhost:4567/delete/" + newId);
    }

    @When("user creates a project")
    public void userCreatesAProject() {
        body = new JSONObject();
    }


    @Then("error code displayed")
    public void errorCodeDisplayed() {
        Assert.assertEquals(response.getStatusCode(), 400, "Status code should be 400, but it's not");
    }

    @And("error message is returned")
    public void errorMessageIsReturned() {
        ResponseBody body1 = response.getBody();
        String bodyAsString = body1.asString();
        System.out.println(bodyAsString);
        Assert.assertEquals(bodyAsString.contains("\"errorMessages\":[\"Could not find field: doneStatus\"]"), true, "Response body contains correct error code");
    }

    @When("user creates a project with {string}")
    public void userCreatesAProjectWith(String arg0) {
        body = new JSONObject();
        body.put("doneStatus", Boolean.parseBoolean(arg0));
    }

}
