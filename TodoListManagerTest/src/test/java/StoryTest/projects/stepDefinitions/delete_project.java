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



public class delete_project {

    private String DummyID;
    private String url;
    private Response response;

    private String wrongUrl;
    JSONObject body;


    @Given("a dummy Project to be amended")
    public void aDummyProjectToBeAmended() {
        JSONObject dumb = new JSONObject();
        dumb.put("title", "project");
        dumb.put("completed", false);
        dumb.put("active", true);
        dumb.put("description", "A project about to be closed");
        Response putRes = RestAssured.given().contentType(ContentType.JSON)
                .body(dumb.toString()).post("http://localhost:4567/projects");
        DummyID = putRes.jsonPath().getString("id");
    }

    @When("I delete the project with id")
    public void iDeleteTheProjectWithId() {
        response = RestAssured.given().delete(url);
    }

    @Then("a success code should be returned")
    public void aSuccessCodeShouldBeReturned() {
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");
    }

    @When("I instantiate the completed to true and active field to false")
    public void iInstantiateTheCompletedToTrueAndActiveFieldToFalse() {
        body = new JSONObject();
        body.put("completed", true);
        body.put("active", false);
        System.out.println(body);
    }

    @And("passes that fields in a post request to the Dummy project")
    public void passesThatFieldsInAPostRequestToTheDummyProject() {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body(body.toString()).post(url);
    }

    @Then("the amend success code is returned")
    public void theAmendSuccessCodeIsReturned() {
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");
    }

    @And("completed and active is modified")
    public void completedAndActiveIsModified() {
        SoftAssert softAssert = new SoftAssert();


        String newCompleted = response.jsonPath().getString("completed");
        softAssert.assertEquals(newCompleted, "true");
        String newActive = response.jsonPath().getString("active");
        softAssert.assertEquals(newActive, "false");


        System.out.println("=======> Printing closed Project: ");
        System.out.println("Title: " + response.jsonPath().getString("title"));
        System.out.println("Completed: " + response.jsonPath().getString("completed"));
        System.out.println("Active: " + response.jsonPath().getString("active"));
        System.out.println("Description: " + response.jsonPath().getString("description"));
    }

    @And("I delete the dummy project")
    public void iDeleteTheDummyProject() {
        RestAssured.given().delete(url);
    }

    @Given("wrong url http:\\/\\/localhost:{int}\\/projects\\/:id")
    public void wrongUrlHttpLocalhostProjectsId(int arg0) {
        url = "http://localhost:4567/projects/" + DummyID;
        wrongUrl = "http://localhost:4567/projects/" + "0";
    }

    @When("I delete the project with invalid id")
    public void iDeleteTheProjectWithInvalidId() {
        response = RestAssured.given().delete(wrongUrl);
    }

    @Then("an error code is returned")
    public void anErrorCodeIsReturned() {
        Assert.assertEquals(response.getStatusCode(), 404, "Status code should be 404, but it's not");
    }

    @Given("a url http:\\/\\/localhost:{int}\\/projects\\/:id")
    public void aUrlHttpLocalhostProjectsId(int arg0) {
        url = "http://localhost:4567/projects/" + DummyID;
    }
}
