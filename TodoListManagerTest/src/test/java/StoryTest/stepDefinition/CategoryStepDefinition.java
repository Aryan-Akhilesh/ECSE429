package StoryTest.stepDefinition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;
import org.custommonkey.xmlunit.XMLAssert;

public class CategoryStepDefinition {

    private final String json = "application/json";
    private final String xml = "application/xml";
    private Response response;
    private String newCategoryId;

    // User scenario 1

    @Given("I have an existing category with field title {string}")
    public void i_have_an_existing_category_with_field_title(String prevTitle) {
        String jsonString = "{\"title\":\"" + prevTitle + "\",\"description\":\"\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
        newCategoryId = response.jsonPath().get("id");
    }

    @When("I update the existing title to a new title {string}")
    public void i_update_the_existing_title_to_a_new_title(String newTitle) {
        String jsonString = "{\"title\":\"" + newTitle + "\",\"description\":\"\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/" + newCategoryId);
    }

    @Then("I should see the title of the category changed to a new title {string}")
    public void i_should_see_the_title_of_the_category_changed_to_a_new_title(String newTitle) {
        response = RestAssured.given().header("Content-Type", json).get("http://localhost:4567/categories/" + newCategoryId);
        String body = "{\"categories\":[{\"id\":\"3\",\"title\":\"" + newTitle + "\",\"description\":\"\"}]}";
        Assertions.assertEquals(body, response.getBody().asString());
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @When("I use Post request to update my existing category title to a new title {string}")
    public void i_use_post_request_to_update_my_existing_category_title_to_a_new_title(String newTitle) {
        String jsonString = "{\"title\":\"" + newTitle + "\",\"description\":\"\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories/" + newCategoryId);
    }

    @When("I update the existing title to a new title {string} with incorrect Id {int}")
    public void i_update_the_existing_title_to_a_new_title_with_incorrect_Id(String newTitle, int invalidCategoryId) {
        String jsonString = "{\"title\":\"" + newTitle + "\",\"description\":\"\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/" + invalidCategoryId);
    }

    // User scenario 2

    @Given("I have an existing category with title {string} and description {string}")
    public void i_have_an_existing_category_with_title_and_description(String prevTitle, String prevDescription) {
        String jsonString = "{\"title\":\"" + prevTitle + "\",\"description\":\"" + prevDescription + "\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
        newCategoryId = response.jsonPath().get("id");
    }

    @When("I replace the title and description field of an existing category to a new title {string} and description {string}")
    public void i_replace_the_title_and_description_field_of_an_existing_category_to_a_new_title_and_description(String newTitle, String newDescription) {
        String jsonString = "{\"title\":\"" + newTitle + "\",\"description\":\"" + newDescription + "\"}";
        RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/" + newCategoryId);
    }

    @Then("I should see the category field replaced to new title {string} and new description {string}")
    public void i_should_see_the_category_field_replaced_to_new_title_and_new_description(String newTitle, String newDescription) {
        response = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories/" + newCategoryId);
        String body = "{\"categories\":[{\"id\":\"" + newCategoryId + "\",\"title\":\"" + newTitle + "\",\"description\":\"" + newDescription + "\"}]}";
        Assertions.assertEquals(body, response.getBody().asString());
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @When("I delete the current existing category")
    public void i_delete_current_existing_category() {
        RestAssured.given().header("Accept", json).delete("http://localhost:4567/categories/" + newCategoryId);
    }

    @And("I create a new category with title {string} and description {string}")
    public void i_create_a_new_category_with_title_and_description(String newTitle, String newDescription) {
        String jsonString = "{\"title\":\"" + newTitle + "\",\"description\":\"" + newDescription + "\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
        newCategoryId = response.jsonPath().get("id");
    }

    @Then("I should see the title and description of the category replaced to a new title {string} and description {string} after deletion")
    public void i_should_see_the_title_and_description_of_the_category_replaced_to_a_new_title_and_description_after_deletion(String newTitle, String newDescription) {
        response = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories/" + newCategoryId);
        String body = "{\"categories\":[{\"id\":\"" + newCategoryId + "\",\"title\":\"" + newTitle + "\",\"description\":\"" + newDescription + "\"}]}";
        Assertions.assertEquals(body, response.getBody().asString());
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @When("I replace the category with a new title {string} and new description {string} with an invalid id {int}")
    public void i_replace_the_category_with_a_new_title_and_new_description_with_an_invalid_id(String newTitle, String newDescription, int invalidCategoryId) {
        String jsonString = "{\"title\":\"" + newTitle + "\",\"description\":\"" + newDescription + "\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).put("http://localhost:4567/categories/" + invalidCategoryId);
    }

    @Then("I should be warned that id {int} is invalid")
    public void i_should_be_warned_that_id_is_invalid(int invalidCategoryId) {
        String error = "{\"errorMessages\":[\"Invalid GUID for " + invalidCategoryId + " entity category\"]}";
        Assertions.assertEquals(error, response.getBody().asString());
        Assertions.assertEquals(404, response.getStatusCode());
    }

    // User scenario 3

    @Given("I do not have a custom category")
    public void i_have_an_non_existing_custom_category() {
        // nothing to do
    }

    @When("I create a new category with title {string} and description field {string}")
    public void i_create_a_new_category_with_title_and_description_field(String newTitle, String newDescription) {
        String jsonString = "{\"title\":\"" + newTitle + "\",\"description\":\"" + newDescription + "\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
        newCategoryId = response.jsonPath().get("id");
    }

    @Then("I should see a new category with new title {string} and description {string}")
    public void i_should_see_a_new_category_with_new_title_and_description(String newTitle, String newDescription) {
        response = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories/" + newCategoryId);
        String body = "{\"categories\":[{\"id\":\"" + newCategoryId + "\",\"title\":\"" + newTitle + "\",\"description\":\"" + newDescription + "\"}]}";
        Assertions.assertEquals(body, response.getBody().asString());
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @When("I create a new category with title {string} only")
    public void i_create_a_new_category_with_title_only(String newTitle) {
        String jsonString = "{\"title\":\"" + newTitle + "\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
        newCategoryId = response.jsonPath().get("id");
    }

    @Then("I should see a new category with title {string} only")
    public void i_should_see_a_new_category_with_title_only(String newTitle) {
        response = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories/" + newCategoryId);
        String body = "{\"categories\":[{\"id\":\"" + newCategoryId + "\",\"title\":\"" + newTitle + "\",\"description\":\"\"}]}";
        Assertions.assertEquals(body, response.getBody().asString());
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @When("I create a new category with an id field {int}")
    public void i_create_a_new_category_with_an_id_field(int categoryId) {
        String jsonString = "{\"id\":\"" + categoryId + "\",\"title\":\"University\",\"description\":\"McGill\"}";
        response = RestAssured.given().header("Content-Type", json).body(jsonString).post("http://localhost:4567/categories");
    }

    @Then("I should be warned that id is not allowed")
    public void i_should_be_warned_that_id_is_not_allowed() {
        String error = "{\"errorMessages\":[\"Invalid Creation: Failed Validation: Not allowed to create with id\"]}";
        Assertions.assertEquals(error, response.getBody().asString());
        Assertions.assertEquals(400, response.getStatusCode());
    }

    // User scenario 4
    @When("I request all category in JSON using valid URL {string}")
    public void i_request_all_category_in_json(String URL) {
        response = RestAssured.given().header("Accept", json).get(URL);
    }

    @Then("I should see all existing categories in JSON")
    public void i_should_see_all_existing_categories_in_json() throws JSONException {
        String body ="{\"categories\":[{\"id\":\"2\",\"title\":\"Home\",\"description\":\"\"},{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        JSONAssert.assertEquals(body,response.getBody().asString(), false);
        Assertions.assertEquals(200,response.getStatusCode());
    }

    @When("I request all category in xml using valid URL {string}")
    public void i_request_all_category_in_xml(String URL) {
        response = RestAssured.given().header("Accept", xml).get(URL);
    }

    @Then("I should see all existing categories in XML")
    public void i_should_see_all_existing_categories_in_xml() {
        Assertions.assertTrue((response.getBody().asString()).matches("<categories>(<category><description\\/><id>1<\\/id><title>Office<\\/title><\\/category><category><description\\/><id>2<\\/id><title>Home<\\/title><\\/category>|<category><description\\/><id>2<\\/id><title>Home<\\/title><\\/category><category><description\\/><id>1<\\/id><title>Office<\\/title><\\/category>)<\\/categories>"));
        Assertions.assertEquals(200,response.getStatusCode());
    }

    @When("I request all categories using invalid url {string}")
    public void i_request_all_categories_using_invalid_url(String invalidURL) {
        response = RestAssured.given().header("Accept", json).get(invalidURL);
    }

    @Then("I should be warned that the url is not found")
    public void i_should_be_warned_that_the_url_is_not_found() {
        Assertions.assertEquals(404, response.getStatusCode());
    }

    // User scenario 5
    @Given("I have an existing category database")
    public void i_have_an_existing_category_database() {
        // do nothing
    }

    @When("I request a category with id {int} in JSON")
    public void i_request_a_category_with_id_in_json(int categoryId) {
        response = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories/" + categoryId);
    }

    @Then("I should see an existing category with id {int} in JSON")
    public void i_should_see_an_existing_category_in_json(int categoryId) {
        String body ="{\"categories\":[{\"id\":\"" + categoryId + "\",\"title\":\"Office\",\"description\":\"\"}]}";
        Assertions.assertEquals(body,response.getBody().asString());
        Assertions.assertEquals(200,response.getStatusCode());
    }

    @When("I request a category with id {int} in XML")
    public void i_request_a_category_with_id_in_xml(int categoryId) {
        response = RestAssured.given().header("Accept", xml).get("http://localhost:4567/categories/" + categoryId);
    }

    @Then("I should see an existing category with id {int} in XML")
    public void i_should_see_an_existing_category_in_xml(int categoryId) {
        String body = "<categories><category><description/><id>" + categoryId + "</id><title>Office</title></category></categories>";
        XMLAssert.assertEquals(body,response.getBody().asString());
        Assertions.assertEquals(200,response.getStatusCode());
    }

    @When("I request a category with an invalid id {int} in JSON")
    public void i_request_a_category_with_an_invalid_id_in_json(int invalidCategoryId) {
        response = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories/" + invalidCategoryId);
    }

    @Then("I should be warned that instance does not exist in category with id {int}")
    public void i_should_be_warned_that_instance_does_not_exist_in_category_with_id(int invalidCategoryId) {
        String error = "{\"errorMessages\":[\"Could not find an instance with categories/" + invalidCategoryId + "\"]}";
        Assertions.assertEquals(404, response.getStatusCode());
        Assertions.assertEquals(response.getBody().asString(), error);
    }

}