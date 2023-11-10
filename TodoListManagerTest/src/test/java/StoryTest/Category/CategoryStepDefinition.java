package StoryTest.Category;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.custommonkey.xmlunit.XMLAssert;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.jar.JarException;

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
    public void i_modify_the_title_of_the_existing_category() {
        String jsonString = "{\"title\":\"Train\",\"description\":\"\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/3");
    }

    @Then("I should see the title of the category changed")
    public void i_should_see_the_title_of_the_category_changed() {
        response = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/3");
        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"Train\",\"description\":\"\"}]}";
        Assertions.assertEquals(body, response.getBody().asString());
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @When("I use Post request to modify my existing category")
    public void i_use_Post_request_to_modify_an_existing_category() {
        String jsonString = "{\"title\":\"Train\",\"description\":\"\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories/3");
    }

//    @Then("I should see the title of the category changed to Plane")
//    public void i_should_see_the_category_title_field_modified_to_Plane() {
//        Response r = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/3");
//        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"Plane\",\"description\":\"\"}]}";
//        Assertions.assertEquals(body, r.getBody().asString());
//    }

    @When("I modify the title of a category by providing an incorrect id")
    public void i_modify_the_title_of_a_category_by_providing_an_incorrect_id() {
        String jsonString = "{\"title\":\"Train\",\"description\":\"\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/300");
    }

    @Then("I should be warned that id is invalid")
    public void i_should_be_warned_that_id_is_invalid() {
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
    public void i_should_see_the_title_and_description_of_the_category_replaced() {
        response = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/3");
        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"University\",\"description\":\"McGill\"}]}";
        Assertions.assertEquals(body, response.getBody().asString());
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @When("I delete the current existing category and create a new one")
    public void i_delete_current_existing_category_and_create_a_new_one() {
        RestAssured.given().header("Content-Type", json).delete("http://localhost:4567/categories/3");
    }

    @And("I create a new category with title and description")
    public void i_create_a_new_category_with_title_and_description() {
        String jsonString = "{\"title\":\"University\",\"description\":\"McGill\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
    }

    @Then("I should see the title and description of the category replaced after deletion")
    public void i_should_see_the_title_and_description_of_the_category_replaced_after_deletion() {
        response = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/3");
        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"University\",\"description\":\"McGill\"}]}";
        Assertions.assertEquals(body, response.getBody().asString());
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @When("I replace the category with an invalid id")
    public void i_replace_the_category_with_an_invalid_id() {
        String jsonString = "{\"title\":\"University\",\"description\":\"McGill\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/300");
    }

//    @Then("I should be warned that id is invalid")
//    public void i_should_be_warned_that_id_is_invalid() {
//        String error = "{\"errorMessages\":[\"Invalid GUID for 300 entity category\"]}";
//        Assertions.assertEquals(error, response.getBody().asString());
//        Assertions.assertEquals(404, response.getStatusCode());
//    }

    // User scenario 3

    @Given("I do not have a custom category")
    public void i_have_an_non_existing_custom_category() {
        // nothing to do
    }

//    @When("I create a new category with title and description field")
//    public void i_create_a_category_with_title_and_description() {
//        String jsonString = "{\"title\":\"University\",\"description\":\"McGill\"}";
//        RestAssured.given().header("Content-Type", json).delete("http://localhost:4567/categories/3");
//        RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
//    }
    @When("I create a new category with an id")
    public void i_create_a_new_category_with_an_id() {
        String jsonString = "{\"id\":\"4\",\"title\":\"University\",\"description\":\"McGill\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
    }

    @Then("I should be warned that id is not allowed")
    public void i_should_be_warned_that_id_is_not_allowed() {
        String error = "{\"errorMessages\":[\"Invalid Creation: Failed Validation: Not allowed to create with id\"]}";
        Assertions.assertEquals(error, response.getBody().asString());
        Assertions.assertEquals(400, response.getStatusCode());
    }

    // User scenario 4
    @When("I request all category in JSON")
    public void i_request_all_category_in_json() {
        response = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories");
    }

    @Then("I should see all existing categories in JSON")
    public void i_should_see_all_existing_categories_in_json() throws JSONException {
        String body ="{\"categories\":[{\"id\":\"2\",\"title\":\"Home\",\"description\":\"\"},{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        JSONAssert.assertEquals(body,response.getBody().asString(), false);
        Assertions.assertEquals(200,response.getStatusCode());
    }

    @When("I request all category in xml")
    public void i_request_all_category_in_xml() {
        response = RestAssured.given().header("Accept", xml).get("http://localhost:4567/categories");
    }

    @Then("I should see all existing categories in XML")
    public void i_should_see_all_existing_categories_in_xml() {
        Assertions.assertTrue((response.getBody().asString()).matches("<categories>(<category><description\\/><id>1<\\/id><title>Office<\\/title><\\/category><category><description\\/><id>2<\\/id><title>Home<\\/title><\\/category>|<category><description\\/><id>2<\\/id><title>Home<\\/title><\\/category><category><description\\/><id>1<\\/id><title>Office<\\/title><\\/category>)<\\/categories>"));
        Assertions.assertEquals(200,response.getStatusCode());
    }

    // User scenario 5
    @When("I request a category with id in JSON")
    public void i_request_a_category_with_id_in_json() {
        response = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories/1");
    }

    @Then("I should see an existing category in JSON")
    public void i_should_see_an_existing_category_in_json() {
        String body ="{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        Assertions.assertEquals(body,response.getBody().asString());
        Assertions.assertEquals(200,response.getStatusCode());
    }

    @When("I request a category with id in XML")
    public void i_request_a_category_with_id_in_xml() {
        response = RestAssured.given().header("Accept", xml).get("http://localhost:4567/categories/1");
    }

    @Then("I should see an existing category in XML")
    public void i_should_see_an_existing_category_in_xml() {
        String body = "<categories><category><description/><id>1</id><title>Office</title></category></categories>";
        XMLAssert.assertEquals(body,response.getBody().asString());
        Assertions.assertEquals(200,response.getStatusCode());
    }

}