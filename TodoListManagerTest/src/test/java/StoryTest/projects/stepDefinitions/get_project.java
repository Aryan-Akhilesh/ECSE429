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


public class get_project {

    private String DummyID;
    private String url;
    private Response response;

    private String wrongUrl;
    JSONObject body;

    private final String xml = "application/xml";


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
