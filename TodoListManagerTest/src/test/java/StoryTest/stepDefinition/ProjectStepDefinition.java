package StoryTest.stepDefinition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class ProjectStepDefinition {

    private String url = null;
    private JSONObject body = null;
    Response response = null;

    private String title = "";
    private String description = "";
    private boolean completed = false;
    private boolean active = false;

    private String DummyID;
    private JSONObject titleBody;
    private Response POSTresponse;

    private String myTitle;
    private String wrongUrl;

    private final String xml = "application/xml";

    private Boolean myCompleted;
    private Boolean myActive;


    @Given("the URL https:\\/\\/localhost:4567\\/projects")
    public void theURLHttpsLocalhostProjects() {
        url = "http://localhost:4567/projects";
    }

    @When("user creates a project with {string}, {string}, {string} and {string}")
    public void userCreatesAProjectWithAllFields(String arg0, String arg1, String arg2, String arg3) {
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
        Assert.assertEquals(bodyAsString.contains("\"errorMessages\":[\"Could not find field: doneStatus\"]"), true, "Response body contains correct error code");
    }

    @When("user creates a project with {string}")
    public void userCreatesAProjectWith(String arg0) {
        body = new JSONObject();
        body.put("doneStatus", Boolean.parseBoolean(arg0));
    }

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


    @And("passes that field in a post request to the Dummy project")
    public void passesThatFieldInAPostRequestToTheDummyProject() {
        POSTresponse = RestAssured.given().contentType(ContentType.JSON)
                .body(titleBody.toString()).post(url);
    }

    @Then("amend success code is returned")
    public void amendSuccessCodeIsReturned() {
        Assert.assertEquals(POSTresponse.getStatusCode(), 200, "Status code should be 200, but it's not");
    }

    @And("title is modified")
    public void titleIsModified() {
        SoftAssert softAssert = new SoftAssert();
        String modifTitle = POSTresponse.jsonPath().getString("title");
        softAssert.assertEquals(modifTitle, myTitle);
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
        softAssert.assertEquals(newCompleted, String.valueOf(myCompleted));
        String newActive = response.jsonPath().getString("active");
        softAssert.assertEquals(newActive, String.valueOf(myActive));
    }

    @When("I delete the project with invalid id")
    public void iDeleteTheProjectWithInvalidId() {
        response = RestAssured.given().delete(url);
    }

    @Then("an error code is returned")
    public void anErrorCodeIsReturned() {
        Assert.assertEquals(response.getStatusCode(), 404, "Status code should be 404, but it's not");
    }

    @Then("all projects are returned")
    public void allProjectsAreReturned() {
        SoftAssert softAssert = new SoftAssert();
        String title = response.jsonPath().getString("title");
        softAssert.assertEquals(title, "Office Work", "Title in response is not expected");

        String id = response.jsonPath().getString("id");
        softAssert.assertEquals(id, "1", "ID in response is not expected");

        String completed = response.jsonPath().getString("completed");
        softAssert.assertEquals(completed, "false", "completed in response is not expected");

        String active = response.jsonPath().getString("active");
        softAssert.assertEquals(active, "false", "Active in response is not expected");

        String description = response.jsonPath().getString("description");
        softAssert.assertEquals(active, "", "Description in response is not expected");
    }

    @And("the expected success code is returned")
    public void theExpectedSuccessCodeIsReturned() {
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");
    }

    @Then("the expected error code is returned")
    public void theExpectedErrorCodeIsReturned() {
        Assert.assertEquals(response.getStatusCode(), 404, "Status code should be 404, but it's not");
    }

    @Then("all projects are returned in xml format")
    public void allProjectsAreReturnedInXmlFormat() {
        SoftAssert softAssert = new SoftAssert();
        String title = response.xmlPath().getString("title");
        softAssert.assertEquals(title, "Office Work", "Title in response is not expected");

        String id = response.xmlPath().getString("id");
        softAssert.assertEquals(id, "1", "ID in response is not expected");

        String completed = response.xmlPath().getString("completed");
        softAssert.assertEquals(completed, "false", "completed in response is not expected");

        String active = response.xmlPath().getString("active");
        softAssert.assertEquals(active, "false", "Active in response is not expected");

        String description = response.xmlPath().getString("description");
        softAssert.assertEquals(active, "", "Description in response is not expected");
    }

    @Given("dummy Project to be got")
    public void dummyProjectToBeGot() {
        JSONObject dumb = new JSONObject();
        dumb.put("title", "project to be got");
        dumb.put("completed", false);
        dumb.put("active", true);
        dumb.put("description", "A project about to be got");
        Response putRes = RestAssured.given().contentType(ContentType.JSON)
                .body(dumb.toString()).post("http://localhost:4567/projects");
        DummyID = putRes.jsonPath().getString("id");
    }

    @When("get a project using get request")
    public void getAProjectUsingGetRequest() {
        response = RestAssured.get(url);
    }

    @Then("the request project is returned")
    public void theRequestProjectIsReturned() {
        SoftAssert softAssert = new SoftAssert();

        String title = response.jsonPath().getString("title");
        softAssert.assertEquals(title, "project to be got", "Title in response is not expected");

        String iden = response.jsonPath().getString("id");
        softAssert.assertEquals(iden, DummyID, "ID in response is not expected");

        String completed = response.jsonPath().getString("completed");
        softAssert.assertEquals(completed, "false", "Completed in response is not expected");

        String active= response.jsonPath().getString("active");
        softAssert.assertEquals(active, "true", "Active in response is not expected");

        String description = response.jsonPath().getString("description");
        softAssert.assertEquals(description, "A project about to be got", "Description in response is not expected");
    }

    @And("success code is returned")
    public void successCodeIsReturned() {
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");
    }

    @When("get a project using get request specific in xml")
    public void getAProjectUsingGetRequestSpecificInXml() {
        response = RestAssured.given().header("Accept",xml).get(url);
    }

    @Then("the request project of xml form is returned")
    public void theRequestProjectOfXmlFormIsReturned() {
        SoftAssert softAssert = new SoftAssert();

        String title = response.xmlPath().getString("title");
        softAssert.assertEquals(title, "project to be got", "Title in response is not expected");

        String iden = response.xmlPath().getString("id");
        softAssert.assertEquals(iden, DummyID, "ID in response is not expected");

        String completed = response.xmlPath().getString("completed");
        softAssert.assertEquals(completed, "false", "Completed in response is not expected");

        String active= response.xmlPath().getString("active");
        softAssert.assertEquals(active, "true", "Active in response is not expected");

        String description = response.xmlPath().getString("description");
        softAssert.assertEquals(description, "A project about to be got", "Description in response is not expected");
    }

    @Then("the error code is returned")
    public void theErrorCodeIsReturned() {
        Assert.assertEquals(response.getStatusCode(), 404, "Status code should be 404, but it's not");
    }

    @And("specific errorMessage is displayed")
    public void specificErrorMessageIsDisplayed() {
        ResponseBody body = response.getBody();
        String bodyAsString = body.asString();
        Assert.assertEquals(bodyAsString.contains("Could not find an instance with projects"), true, "Response body contains correct error code");
    }

    @When("get a project using get request with wrong url")
    public void getAProjectUsingGetRequestWithWrongUrl() {
        response = RestAssured.get(wrongUrl);
    }

    @When("I instantiate the title field with {string}")
    public void iInstantiateTheTitleFieldWith(String arg0) {
        myTitle = arg0;
        titleBody = new JSONObject();
        titleBody.put("title", myTitle);
    }

    @Given("a url http:\\/\\/localhost:{int}\\/projects\\/:id where id = {string}")
    public void aUrlHttpLocalhostProjectsIdWhereId(int arg0, String arg1) {
        url = "http://localhost:4567/projects/" + arg1;
    }

    @When("I instantiate the completed to {string} and active field to {string}")
    public void iInstantiateTheCompletedToAndActiveFieldTo(String arg0, String arg1) {
        body = new JSONObject();
        myCompleted = Boolean.parseBoolean(arg0);
        myActive = Boolean.parseBoolean(arg0);
        body.put("completed", myCompleted);
        body.put("active", myActive);
    }

    @And("passes that fields in a post request to the project")
    public void passesThatFieldsInAPostRequestToTheProject() {
        response = RestAssured.given().contentType(ContentType.JSON)
                .body(body.toString()).post(url);
    }

    @When("I instantiate the topic field with {string}")
    public void iInstantiateTheTopicFieldWith(String arg0) {
        titleBody = new JSONObject();
        titleBody.put("topic", arg0);
    }

    @Given("user performs GET request on the url {string}")
    public void userPerformsGETRequestOnTheUrl(String arg0) {
        url = arg0;
        response = RestAssured.given().get(url);
    }

    @Given("user performs GET request on the url {string} with xml header")
    public void userPerformsGETRequestOnTheUrlWithXmlHeader(String arg0) {
        url = arg0;
        response = RestAssured.given().header("Accept",xml).get(url);
    }

    @Given("target url http:\\/\\/localhost:{int}\\/projects\\/:id with id = {string}")
    public void targetUrlHttpLocalhostProjectsIdWithId(int arg0, String arg1) {
        url = "http://localhost:4567/projects/" + arg1;
    }

    @Given("wrong target url http:\\/\\/localhost:{int}\\/projects\\/:id with id = {string}")
    public void wrongTargetUrlHttpLocalhostProjectsIdWithId(int arg0, String arg1) {
        wrongUrl = "http://localhost:4567/projects/" + arg1;
    }
}
