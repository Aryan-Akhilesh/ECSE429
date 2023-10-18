import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FailingTest {

    private final String json = "application/json";
    private final String xml = "application/xml";

    @BeforeAll
    static void startServer() {
        ProcessBuilder pb;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows")) {
            pb = new ProcessBuilder(
                    "cmd.exe", "/c", "java -jar .\\src\\test\\resources\\runTodoManagerRestAPI-1.5.5.jar");
        }
        else {
            pb = new ProcessBuilder(
                    "sh", "-c", "java -jar .\\src\\test\\resources\\runTodoManagerRestAPI-1.5.5.jar");
        }
        try {
            pb.start();
        } catch (IOException e) {
            System.out.println("No server");
        }
        RestAssured.baseURI = "http://localhost:4567";
    }

    @AfterAll
    static void shutServer() {
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
}
