import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.io.IOException;

@TestMethodOrder(MethodOrderer.Random.class)
public class InteroperabilityTest {

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

    // TODOS -> CATEGORIES

    @Test
    public void getCategoriesByTodoValidIdJson() {
        Response r = RestAssured.given()
                        .header("Accept", json)
                        .get("http://localhost:4567/todos/1/categories");
        int statusCode = r.getStatusCode();
        String body = "{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void getCategoriesByTodoValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .get("http://localhost:4567/todos/1/categories");
        int statusCode = r.getStatusCode();
        String body = "<categories><category><description/><id>1</id><title>Office</title></category></categories>";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    // Success with weird behavior
    public void getCategoriesByTodoInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/todos/99/categories");
        int statusCode = r.getStatusCode();
        String body = "{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void headCategoriesByTodoValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/todos/1/categories");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void headCategoriesByTodoValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .head("http://localhost:4567/todos/1/categories");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    // Success with weird behavior
    public void headCategoriesByTodoInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/todos/99/categories");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void postCategoriesByTodoValidIdJson() {
        Response r = RestAssured.given()
                .header("Content-Type", json)
                .body("{ \"id\": \"2\"}")
                .post("http://localhost:4567/todos/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(201, statusCode);
    }

    @Test
    public void postCategoriesByTodoValidIdJsonMalformed() {
        Response r = RestAssured.given()
                .header("Content-Type", json)
                .body("{ \"id\":- \"2\"}")
                .post("http://localhost:4567/todos/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    // Cannot find category by id using xml
    public void postCategoriesByTodoValidIdXml() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<category><id>1</id></category>")
                .post("http://localhost:4567/todos/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void postCategoriesByTodoValidIdXmlMalformed() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<category>3<id>1</id></category>")
                .post("http://localhost:4567/todos/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    public void deleteCategoriesByTodoValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .delete("http://localhost:4567/todos/1/categories/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void deleteCategoriesByTodoInValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .delete("http://localhost:4567/todos/1/categories/23");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void deleteCategoriesByTodoValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .delete("http://localhost:4567/todos/1/categories/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void deleteCategoriesByTodoInValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .delete("http://localhost:4567/todos/1/categories/23");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    // PROJECTS -> CATEGORIES

    @Test
    public void getCategoriesByProjectValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/projects/1/categories");
        int statusCode = r.getStatusCode();
        String body = "{\"categories\":[]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void getCategoriesByProjectValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .get("http://localhost:4567/projects/1/categories");
        int statusCode = r.getStatusCode();
        String body = "<categories></categories>";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    // Success with weird behavior
    public void getCategoriesByProjectInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/projects/99/categories");
        int statusCode = r.getStatusCode();
        String body = "{\"categories\":[]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void headCategoriesByProjectValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/projects/1/categories");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void headCategoriesByProjectValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .head("http://localhost:4567/projects/1/categories");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    // Success with weird behavior
    public void headCategoriesByProjectInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/projects/99/categories");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void postCategoriesByProjectValidIdJson() {
        Response r = RestAssured.given()
                .header("Content-Type", json)
                .body("{ \"id\": \"1\"}")
                .post("http://localhost:4567/projects/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(201, statusCode);
    }

    @Test
    public void postCategoriesByProjectValidIdJsonMalformed() {
        Response r = RestAssured.given()
                .header("Content-Type", json)
                .body("{ \"id\":- \"2\"}")
                .post("http://localhost:4567/projects/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    // Cannot find category by id using xml
    public void postCategoriesByProjectValidIdXml() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<category><id>1</id></category>")
                .post("http://localhost:4567/projects/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void postCategoriesByProjectValidIdXmlMalformed() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<category>3<id>1</id></category>")
                .post("http://localhost:4567/projects/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    public void deleteCategoriesByProjectValidIdJson() {
        RestAssured.given().header("Content-Type", json).body("{ \"id\": \"1\"}").post("http://localhost:4567/projects/1/categories");
        Response r = RestAssured.given()
                .header("Accept", json)
                .delete("http://localhost:4567/projects/1/categories/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void deleteCategoriesByProjectInValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .delete("http://localhost:4567/projects/1/categories/23");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void deleteCategoriesByProjectValidIdXml() {
        RestAssured.given().header("Content-Type", json).body("{ \"id\": \"1\"}").post("http://localhost:4567/projects/1/categories");
        Response r = RestAssured.given()
                .header("Accept", xml)
                .delete("http://localhost:4567/projects/1/categories/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void deleteCategoriesByProjectInValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .delete("http://localhost:4567/projects/1/categories/23");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    // CATEGORIES -> PROJECTS

    @Test
    public void getProjectsByCategoryValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        String body = "{\"projects\":[]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void getProjectsByCategoryValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .get("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        String body = "<projects></projects>";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    // Success with weird behavior
    public void getProjectsByCategoryInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/categories/65/projects");
        int statusCode = r.getStatusCode();
        String body = "{\"projects\":[]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void headProjectsByCategoryValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void headProjectsByCategoryValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .head("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    // Success with weird behavior
    public void headProjectsByCategoryInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/categories/57/projects");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void postProjectsByCategoryValidIdJson() {
        Response r = RestAssured.given()
                .header("Content-Type", json)
                .body("{ \"id\": \"1\"}")
                .post("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(201, statusCode);
    }

    @Test
    public void postProjectsByCategoryValidIdJsonMalformed() {
        Response r = RestAssured.given()
                .header("Content-Type", json)
                .body("{ \"id\":- \"2\"}")
                .post("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    // Cannot find category by id using xml
    public void postProjectsByCategoryValidIdXml() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<project><id>1</id></project>")
                .post("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void postProjectsByCategoryValidIdXmlMalformed() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<project>3<id>1</id></project>")
                .post("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    public void deleteProjectsByCategoryValidIdJson() {
        RestAssured.given().header("Content-Type", json).body("{ \"id\": \"1\"}").post("http://localhost:4567/categories/1/projects");
        Response r = RestAssured.given()
                .header("Accept", json)
                .delete("http://localhost:4567/categories/1/projects/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void deleteProjectsByCategoryInValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .delete("http://localhost:4567/categories/1/projects/89");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void deleteProjectsByCategoryValidIdXml() {
        RestAssured.given().header("Content-Type", json).body("{ \"id\": \"1\"}").post("http://localhost:4567/categories/1/projects");
        Response r = RestAssured.given()
                .header("Accept", xml)
                .delete("http://localhost:4567/categories/1/projects/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void deleteProjectsByCategoryInValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .delete("http://localhost:4567/categories/1/projects/89");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    // CATEGORIES -> TODOS

    @Test
    public void getTodosByCategoryValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/categories/1/todos");
        int statusCode = r.getStatusCode();
        String body = "{\"todos\":[]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void getTodosByCategoryValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .get("http://localhost:4567/categories/1/todos");
        int statusCode = r.getStatusCode();
        String body = "<todos></todos>";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    // Success with weird behavior
    public void getTodosByCategoryInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/categories/45/todos");
        int statusCode = r.getStatusCode();
        String body = "{\"todos\":[]}";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void headTodosByCategoryValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/categories/1/todos");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void headTodosByCategoryValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .head("http://localhost:4567/categories/1/todos");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    // Success with weird behavior
    public void headTodosByCategoryInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/categories/96/todos");
        int statusCode = r.getStatusCode();
        String body = "";
        Assertions.assertEquals(body, r.getBody().asString());
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void postTodosByCategoryValidIdJson() {
        Response r = RestAssured.given()
                .header("Content-Type", json)
                .body("{ \"id\": \"1\"}")
                .post("http://localhost:4567/categories/1/todos");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(201, statusCode);
    }

    @Test
    public void postTodosByCategoryValidIdJsonMalformed() {
        Response r = RestAssured.given()
                .header("Content-Type", json)
                .body("{ \"id\":- \"2\"}")
                .post("http://localhost:4567/categories/1/todos");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    // Cannot find category by id using xml
    public void postTodosByCategoryValidIdXml() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<todo><id>1</id></todo>")
                .post("http://localhost:4567/categories/1/todos");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void postTodosByCategoryValidIdXmlMalformed() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<todo>3<id>1</id></todo>")
                .post("http://localhost:4567/categories/1/todos");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(400, statusCode);
    }

    @Test
    public void deleteTodosByCategoryValidIdJson() {
        RestAssured.given().header("Content-Type", json).body("{ \"id\": \"1\"}").post("http://localhost:4567/categories/1/todos");
        Response r = RestAssured.given()
                .header("Accept", json)
                .delete("http://localhost:4567/categories/1/todos/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void deleteTodosByCategoryInValidIdJson() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .delete("http://localhost:4567/categories/1/todos/89");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void deleteTodosByCategoryValidIdXml() {
        RestAssured.given().header("Content-Type", json).body("{ \"id\": \"1\"}").post("http://localhost:4567/categories/1/todos");
        Response r = RestAssured.given()
                .header("Accept", xml)
                .delete("http://localhost:4567/categories/1/todos/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void deleteTodosByCategoryInValidIdXml() {
        Response r = RestAssured.given()
                .header("Accept", xml)
                .delete("http://localhost:4567/categories/1/todos/89");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }
}
