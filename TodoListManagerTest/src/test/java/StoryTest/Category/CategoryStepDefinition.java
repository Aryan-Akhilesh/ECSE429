package StoryTest.Category;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;

public class CategoryStepDefinition {

    private final String json = "application/json";
    private final String xml = "application/xml";
    private Response response;

    // User scenario 1

    @Given("I have an existing category with field title")
    public void i_have_an_existing_category_with_field_title() {
        String jsonString = "{\"title\":\"Car\",\"description\":\"\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
    }

    @When("I modify the title of the existing category")
    public void i_modify_the_title_of_an_existing_category() {
        String jsonString = "{\"title\":\"Train\",\"description\":\"\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/3");
    }

    @Then("I should see the title of the category changed to train")
    public void i_should_see_the_category_title_field_modified_to_Train() {
        Response r = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/3");
        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"Train\",\"description\":\"\"}]}";
        Assertions.assertEquals(body, r.getBody().asString());
    }

    @When("I use Post request to modify my existing category")
    public void i_modify_the_title_of_an_existing_category_using_POST() {
        String jsonString = "{\"title\":\"Plane\",\"description\":\"\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories/3");
    }

    @Then("I should see the title of the category changed to Plane")
    public void i_should_see_the_category_title_field_modified_to_Plane() {
        Response r = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/3");
        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"Plane\",\"description\":\"\"}]}";
        Assertions.assertEquals(body, r.getBody().asString());
    }

    @When("I modify the title of a category by providing an incorrect id")
    public void i_modify_the_title_of_a_category_using_incorrect_id() {
        String jsonString = "{\"title\":\"Jet\",\"description\":\"\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/300");
    }

    @Then("I should see that the title of the category is not modified")
    public void i_should_see_the_title_not_modified() {
        Response r = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/3");
        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"Plane\",\"description\":\"\"}]}";
        Assertions.assertEquals(body, r.getBody().asString());
    }

    // User scenario 2




}