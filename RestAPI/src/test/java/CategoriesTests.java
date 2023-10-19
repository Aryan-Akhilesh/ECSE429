import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.io.IOException;

@TestMethodOrder(MethodOrderer.Random.class)
public class CategoriesTests{

    private static ProcessBuilder pb;

    private final String json = "application/json";
    private final String xml = "application/xml";

    @BeforeAll
    public void setupProcess() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("window")) {
            pb = new ProcessBuilder(
                    "cmd.exe", "/c", "jave -jar .\\src\\test\\resources\\runTodoManagerRestAPI-1.5.5.jar"
            );
        }
        else {
            pb = new ProcessBuilder(
                    "sh", "-c", "java -jar ./src/test/resources/runTodoManagerRestAPI-1.5.5.jar"
            );
        }
    }

    @BeforeEach
    void startServer() throws InterruptedException {
        try {
            pb.start();
            Thread.sleep(500);
        } catch (IOException e) {
            System.out.println("NO SERVER");
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
    public void getAllCategoriesJson() {
        Response r = RestAssured.given().header("Accept", json).get("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body ="{\"categories\":[{\"id\":\"2\",\"title\":\"Home\",\"description\":\"\"},{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void getAllCategoriesXml() {
        Response r = RestAssured.given().header("Accept", xml).get("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body ="<categories><category><description/><id>2</id><title>Home</title></category><category><description/><id>1</id><title>Office</title></category></categories>";
        Assertions.assertEquals(body,r.getBody().asString());
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
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

    public void getCategoriesValidIdJson() {
        Response r = RestAssured.given().header("Accept", xml).get("http://localhost:4567/categories/1");
        int statusCode = r.getStatusCode();
        String body = "{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    public void getCategoriesValidIdXml() {
        Response r = RestAssured.given().header("Accept", xml).get("http://localhost:4567/categories/1");
        int statusCode = r.getStatusCode();
        String body = "<categories><category><description/><id>1</id><title>Office</title></category></categories>";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    public void updateCategoriesWithoutIdJson() {
        Response r = RestAssured.given().header("Accept", json).put("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(405, statusCode);
    }

    public void updateCategoriesWithoutIdXml() {
        Response r = RestAssured.given().header("Accept", xml).put("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(405, statusCode);
    }

    public void createNewCategoriesWithNoFieldJson() {
        Response r = RestAssured.given().header("Accept", json).body("{}").post("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body = "{\"errorMessages\":[\"title : field is mandatory\"]}";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(400,statusCode);
    }

    public void createNewCategoriesWithNoFieldXml() {
        Response r = RestAssured.given().header("Accept", xml).body("{}").post("http://localhost:4567/categories");
        int statusCode = r.getStatusCode();
        String body = "<errorMessages><errorMessage>title : field is mandatory</errorMessage></errorMessages>";
        Assertions.assertEquals(body,r.getBody().asString());
        Assertions.assertEquals(400,statusCode);
    }



}