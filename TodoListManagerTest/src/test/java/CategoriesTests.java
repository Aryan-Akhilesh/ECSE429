import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.custommonkey.xmlunit.XMLAssert;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.Random.class)
public class CategoriesTests{

    private static ProcessBuilder pb;

    private final String json = "application/json";
    private final String xml = "application/xml";

    @BeforeAll
    static void setupProcess() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows")) {
            pb = new ProcessBuilder(
                    "cmd.exe", "/c", "java -jar .\\src\\test\\resources\\runTodoManagerRestAPI-1.5.5.jar");
        }
        else {
            pb = new ProcessBuilder(
                    "sh", "-c", "java -jar ./src/test/resources/runTodoManagerRestAPI-1.5.5.jar");
        }
    }

    @BeforeEach
    void startServer() throws InterruptedException {
        try {
            pb.start();
            Thread.sleep(1000);
        } catch (IOException e) {
            System.out.println("No server");
        }
    }

    @AfterEach
    void shutServer() {
        try {
            RestAssured.get("http://localhost:4567/shutdown");
        }
        catch (Exception ignored) {
        }
    }

    @Test
    public void getAllCategoriesJson() throws JSONException {
        Response r = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body ="{\"categories\":[{\"id\":\"2\",\"title\":\"Home\",\"description\":\"\"},{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        JSONAssert.assertEquals(body,r.getBody().asString(), false);
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void getCategoriesInvalidIdJson() {
        Response r = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories/0");
        int statusCode = r.getStatusCode();
        String body = "{\"errorMessages\":[\"Could not find an instance with categories/0\"]}";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

    @Test
    public void getCategoriesInvalidIdXml() {
        Response r = RestAssured.given().header("Accept", xml).get("http://localhost:4567/categories/0");
        int statusCode = r.getStatusCode();
        String body = "<errorMessages><errorMessage>Could not find an instance with categories/0</errorMessage></errorMessages>";
        XMLAssert.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

    @Test
    public void getCategoriesValidIdJson() {
        Response r = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories/1");
        int statusCode = r.getStatusCode();
        String body = "{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void getCategoriesValidIdXml() {
        Response r = RestAssured.given().header("Accept", xml).get("http://localhost:4567/categories/1");
        int statusCode = r.getStatusCode();
        String body = "<categories><category><description/><id>1</id><title>Office</title></category></categories>";
        XMLAssert.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void updateCategoriesWithoutIdJson() {
        Response r = RestAssured.given().header("Accept", json).put("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(405, statusCode);
    }

    @Test
    public void updateCategoriesWithoutIdXml() {
        Response r = RestAssured.given().header("Accept", xml).put("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(405, statusCode);
    }

    @Test
    public void createNewCategoriesWithNoFieldJson() {
        Response r = RestAssured.given().header("Accept", json).body("{}").post("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body = "{\"errorMessages\":[\"title : field is mandatory\"]}";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(400,statusCode);
    }

    @Test
    public void createNewCategoriesWithNoFieldXml() {
        Response r = RestAssured.given().header("Content-Type", xml).header("Accept", xml).body("").post("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body = "<errorMessages><errorMessage>title : field is mandatory</errorMessage></errorMessages>";
        XMLAssert.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(400,statusCode);
    }

    @Test
    public void deleteCategoryWithNoIdJson() {
        Response r = RestAssured.given().header("Accept", json).delete("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(405,statusCode);
    }

    @Test
    public void deleteCategoryWithNoIdXml() {
        Response r = RestAssured.given().header("Accept", xml).delete("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body = "";
        XMLAssert.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(405,statusCode);
    }

    @Test
    public void createNewCategoryWithTitleJson() {
        Response r = RestAssured.given().header("Accept", json).body("{\"title\": \"new category\"}").post("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body = "{\"id\":\"3\",\"title\":\"new category\",\"description\":\"\"}";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(201,statusCode);
    }

    @Test
    public void createNewCategoryWithTitleXml() {
        Response r = RestAssured.given().header("Content-Type", xml).header("Accept", xml).body("<category><title>new category</title></category>").post("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body = "<category><description/><id>3</id><title>new category</title></category>";
        XMLAssert.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(201,statusCode);
    }

    @Test
    public void updateCategoryWithValidFieldAndIdJson() {
        Response r = RestAssured.given().header("Accept", json).body("{\"title\": \"new category\"}").put("http://localhost:4567/categories/2");
        int statusCode = r.getStatusCode();
        String body = "{\"id\":\"2\",\"title\":\"new category\",\"description\":\"\"}";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void updateCategoryWithValidFieldAndIdXml() {
        Response r = RestAssured.given().header("Content-Type", xml).header("Accept", xml).body("<category><title>new category</title></category>").put("http://localhost:4567/categories/2");
        int statusCode = r.getStatusCode();
        String body = "<category><description/><id>2</id><title>new category</title></category>";
        XMLAssert.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void updateCategoryWithInvalidFieldJson() {
        Response r = RestAssured.given().header("Accept", json).body("{\"name\": \"new category\"}").put("http://localhost:4567/categories/2");
        int statusCode = r.getStatusCode();
        String body = "{\"errorMessages\":[\"Could not find field: name\"]}";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(400,statusCode);
    }

    @Test
    public void updateCategoryWithInvalidFieldXml() {
        Response r = RestAssured.given().header("Content-Type", xml).header("Accept", xml).body("<category><name>new category</name></category>").put("http://localhost:4567/categories/2");
        int statusCode = r.getStatusCode();
        String body = "<errorMessages><errorMessage>Could not find field: name</errorMessage></errorMessages>";
        XMLAssert.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(400,statusCode);
    }

    @Test
    public void deleteCategoryWithExistingIdJson() {
        Response r = RestAssured.given().header("Accept", json).delete("http://localhost:4567/categories/2");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void deleteCategoryWithExistingIdXml() {
        Response r = RestAssured.given().header("Accept", xml).delete("http://localhost:4567/categories/2");
        int statusCode = r.getStatusCode();
        String body = "";
        XMLAssert.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void createCategoryWithExistingIdJson() {
        Response r = RestAssured.given().header("Accept", json).body("{\"title\":\"new category\"}").post("http://localhost:4567/categories/1");
        int statusCode = r.getStatusCode();
        String body = "{\"id\":\"1\",\"title\":\"new category\",\"description\":\"\"}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void createCategoryWithExistingIdXml() {
        Response r = RestAssured.given().header("Content-Type", xml).header("Accept", xml).body("<category><title>new category</title></category>").post("http://localhost:4567/categories/1");
        int statusCode = r.getStatusCode();
        String body = "<category><description/><id>1</id><title>new category</title></category>";
        XMLAssert.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void createNewCategoryWithInvalidIdJson() {
        Response r = RestAssured.given().header("Accept", json).body("{\"title\":\"new category\"}").post("http://localhost:4567/categories/10");
        int statusCode = r.getStatusCode();
        String body = "{\"errorMessages\":[\"No such category entity instance with GUID or ID 10 found\"]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

    @Test
    public void createNewCategoryWithInvalidIdXml() {
        Response r = RestAssured.given().header("Content-Type", xml).header("Accept", xml).body("<category><title>new category</title></category>").post("http://localhost:4567/categories/10");
        int statusCode = r.getStatusCode();
        String body = "<errorMessages><errorMessage>No such category entity instance with GUID or ID 10 found</errorMessage></errorMessages>";
        XMLAssert.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

    @Test
    public void headCategoriesWithNoId() {
        Response r = RestAssured.given().head("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,200);
    }

    @Test
    public void headCategoriesWithValidId() {
        Response r = RestAssured.given().head("http://localhost:4567/categories/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,200);
    }

    @Test
    public void headCategoriesWithInvalidId() {
        Response r = RestAssured.given().head("http://localhost:4567/categories/10");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void optionCategoriesWithNoId() {
        Response r = RestAssured.given().options("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,200);
    }

    @Test
    public void optionCategoriesWithValidId() {
        Response r = RestAssured.given().options("http://localhost:4567/categories/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,200);
    }

    @Test
    public void patchCategoriesWithNoId() {
        Response r = RestAssured.given().patch("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,405);
    }

    @Test
    public void patchCategoriesWithValidId() {
        Response r = RestAssured.given().patch("http://localhost:4567/categories/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,405);
    }

    @Test
    public void patchCategoriesWithInvalidId() {
        Response r = RestAssured.given().patch("http://localhost:4567/categories/10");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,405);
    }
}