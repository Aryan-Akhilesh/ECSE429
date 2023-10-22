import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.Random.class)
public class ProjectsFailingTest {

    private static final String pUrl = "http://localhost:4567/projects";
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


    //Post a new Project with an ID should be allowed, however it is not.
    @Test
    public void postProjectWithIDJsonFailing(){
        String requestBody = "{\n" +
                "    \"id\": 2\n" +
                "}";
        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).post(pUrl);
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }
    @Test
    public void postProjectWithIDXmlFailing(){
        String requestBody = "<id>2</id>";
        Response r1 = RestAssured.given().header("Content-Type",xml).header("Accept",xml).body(requestBody).post(pUrl);
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }


    //Undocumented request 'patch' not supported
    @Test
    public void patchProjectFailing(){
        String requestBody = "";
        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).patch(pUrl+"/1");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

    //Test to get the tasks of a non-existing project. It produces unexpected result and statuscode.
    @Test
    public void getProjectTaskNonExistingProjectFailing() throws JSONException {
        Response r1 = RestAssured.given().header("Accept",json).body("").get(pUrl+"/0/tasks");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

}