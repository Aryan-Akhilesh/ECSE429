import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;

public class InteroperabilityFailingTest {
    /**
     * ALL TESTS HERE ARE EXPECTED TO FAIL!
     */

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
            Thread.sleep(300);
        } catch (IOException e) {
            System.out.println("No server");
        }
        RestAssured.baseURI = "http://localhost:4567";
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
    public void getCategoriesByTodoInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/todos/99/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void headCategoriesByTodoInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/todos/99/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void postCategoriesByTodoValidIdXml() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<category><id>1</id></category>")
                .post("http://localhost:4567/todos/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(201, statusCode);
    }

    @Test
    public void getProjectsByTodoInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/projects/99/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    // Success with weird behavior
    public void headProjectsByTodoInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/projects/99/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void postCategoriesByProjectValidIdXml() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<category><id>1</id></category>")
                .post("http://localhost:4567/projects/1/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(201, statusCode);
    }

    @Test
    public void getProjectsByCategoryInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void headProjectsByCategoryInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void postProjectsByCategoryValidIdXml() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<project><id>1</id></project>")
                .post("http://localhost:4567/categories/1/projects");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(201, statusCode);
    }

    @Test
    public void getTodosByCategoryInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .get("http://localhost:4567/categories/45/todos");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void headTodosByCategoryInvalidId() {
        Response r = RestAssured.given()
                .header("Accept", json)
                .head("http://localhost:4567/categories/96/todos");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void postTodosByCategoryValidIdXml() {
        Response r = RestAssured.given()
                .header("Content-Type", xml)
                .body("<todo><id>1</id></todo>")
                .post("http://localhost:4567/categories/1/todos");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(201, statusCode);
    }
}
