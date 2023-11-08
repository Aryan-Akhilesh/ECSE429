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


public class get_all_project {

    private String DummyID;
    private String url;
    private Response response;

    private String wrongUrl;
    JSONObject body;

    private final String xml = "application/xml";

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
}
