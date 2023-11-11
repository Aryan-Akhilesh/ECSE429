package StoryTest.Projects;

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


    private String wrongUrl;

    private final String xml = "application/xml";






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


    @Given("user performs GET request on the url: http:\\/\\/localhost:{int}\\/projects\\/")
    public void userPerformsGETRequestOnTheUrlHttpLocalhostProjects(int arg0) {
        url = "http://localhost:4567/projects";
        response = RestAssured.get(url);
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

    @Given("user performs GET request on the url: http:\\/\\/localhost:{int}\\/projects\\/ with xml header")
    public void userPerformsGETRequestOnTheUrlHttpLocalhostProjectsWithXmlHeader(int arg0) {
        url = "http://localhost:4567/projects";
        response = RestAssured.given().header("Accept",xml).get(url);
    }

    @Given("invalid url")
    public void invalidUrl() {
        url = "http://localhost:4567/project";
    }

    @And("user performs GET request on the url")
    public void userPerformsGETRequestOnTheUrl() {
        response = RestAssured.get(url);
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

    @Given("target url http:\\/\\/localhost:{int}\\/projects\\/:id")
    public void targetUrlHttpLocalhostProjectsId(int arg0) {
        url = "http://localhost:4567/projects/" + DummyID;
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

    @And("I delete the target dummy project")
    public void iDeleteTheTargetDummyProject() {
        RestAssured.given().delete(url);
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

    @Given("wrong target url http:\\/\\/localhost:{int}\\/projects\\/:id")
    public void wrongTargetUrlHttpLocalhostProjectsId(int arg0) {
        url = "http://localhost:4567/projects/" + DummyID;
        wrongUrl = "http://localhost:4567/projects/" + "0";
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

}
