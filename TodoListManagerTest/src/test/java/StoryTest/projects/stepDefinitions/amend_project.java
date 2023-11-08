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


public class amend_project {

    private String DummyID;
    private String url;
    private JSONObject titleBody;
    private Response POSTresponse;


    @Given("dummy Project to be amended")
    public void dummyProjectToBeAmended() {
        JSONObject body = new JSONObject();
        body.put("title", "to be changed");
        body.put("completed", false);
        body.put("active", false);
        body.put("description", "A project with a title to be changed");
        Response putRes = RestAssured.given().contentType(ContentType.JSON)
                .body(body.toString()).post("http://localhost:4567/projects");
        DummyID = putRes.jsonPath().getString("id");
    }

    @Given("http:\\/\\/localhost:{int}\\/projects\\/:id")
    public void http_localhost_projects_id(Integer int1) {
        url = "http://localhost:4567/projects/" + DummyID;
    }

    @When("I instantiate the title field")
    public void iInstantiateTheTitleField() {
        titleBody = new JSONObject();
        titleBody.put("title", "new title");
    }

    @And("passes that field in a post request to the Dummy project")
    public void passesThatFieldInAPostRequestToTheDummyProject() {
        POSTresponse = RestAssured.given().contentType(ContentType.JSON)
                .body(titleBody.toString()).post(url);
    }

    @Then("amend success code is returned")
    public void amendeSuccessCodeIsReturned() {
        Assert.assertEquals(POSTresponse.getStatusCode(), 200, "Status code should be 200, but it's not");
    }

    @And("title is modified")
    public void titleIsModified() {
        SoftAssert softAssert = new SoftAssert();
        String modifTitle = POSTresponse.jsonPath().getString("title");
        softAssert.assertEquals(modifTitle, "new title");
    }

    @And("I delete dummy project")
    public void iDeleteDummyProject() {
        RestAssured.given().delete(url);
    }

    @And("passes that field in a put request to the Dummy project")
    public void passesThatFieldInAPutRequestToTheDummyProject() {
        POSTresponse = RestAssured.given().contentType(ContentType.JSON)
                .body(titleBody.toString()).put(url);
    }



    @Then("amend error code is returned")
    public void amendErrorCodeIsReturned() {
        Assert.assertEquals(POSTresponse.getStatusCode(), 400, "Status code should be 400, but it's not");
    }

    @And("amend error message is displayed")
    public void amendErrorMessageIsDisplayed() {
        ResponseBody body1 = POSTresponse.getBody();
        String bodyAsString = body1.asString();
        Assert.assertEquals(bodyAsString.contains("\"errorMessages\":[\"Could not find field: topic\"]"), true, "Response body does not correspond to expected result");
    }

    @And("I delete dummy todo")
    public void iDeleteDummyTodo() {
    }

    @When("I instantiate the topic field")
    public void iInstantiateTheTopicField() {
        titleBody = new JSONObject();
        titleBody.put("topic", "new topic");
    }
}
