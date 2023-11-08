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
        Assertions.assertEquals(200, r.getStatusCode());

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

    @Then("I should be warned that id is invalid")
    public void i_should_see_warning_on_invalid_id() {
        String error = "{\"errorMessages\":[\"Invalid GUID for 300 entity category\"]}";
        Assertions.assertEquals(error, response.getBody().asString());
        Assertions.assertEquals(404, response.getStatusCode());
    }

    // User scenario 2

    @Given("I have an existing category with valid field")
    public void i_have_an_existing_category_with_valid_field() {
        String jsonString = "{\"title\":\"School\",\"description\":\"university\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
    }

    @When("I replace the title and description field of the existing category")
    public void i_replace_the_title_and_description_field_of_an_existing_category() {
        String jsonString = "{\"title\":\"University\",\"description\":\"McGill\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/3");
    }

    @Then("I should see the title and description of the category replaced")
    public void i_should_see_the_category_title_description_field_replaced() {
        Response r = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/3");
        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"University\",\"description\":\"McGill\"}]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, r.getStatusCode());
    }

    @When("I delete the current existing category and create a new one")
    public void i_delete_current_existing_category_and_create_a_new_one() {
        String jsonString = "{\"title\":\"University\",\"description\":\"McGill\"}";
        RestAssured.given().header("Content-Type", json).delete("http://localhost:4567/categories/3");
        RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
    }

    @Then("I should see the title and description of the category replaced after deletion")
    public void i_should_see_the_category_title_description_field_replaced_after_deletion() {
        Response r = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/3");
        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"University\",\"description\":\"McGill\"}]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, r.getStatusCode());
    }

    @When("I replace the category with an invalid id")
    public void i_replace_category_with_incorrect_id() {
        String jsonString = "{\"title\":\"University\",\"description\":\"McGill\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/300");
    }

    @Then("I should be warned that id is invalid")
    public void i_should_see_warning_on_invalid_id_provided() {
        String error = "{\"errorMessages\":[\"Invalid GUID for 300 entity category\"]}";
        Assertions.assertEquals(error, response.getBody().asString());
        Assertions.assertEquals(404, response.getStatusCode());
    }

    // User scenario 3

    @Given("I do not have a custom category")
    public void i_have_an_non_existing_custom_category() {
        // nothing to do
    }

    @When("I create a new category with title and description field")
    public void i_create_a_category_with_title_and_description() {
        String jsonString = "{\"title\":\"University\",\"description\":\"McGill\"}";
        RestAssured.given().header("Content-Type", json).delete("http://localhost:4567/categories/3");
        RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
    }










}