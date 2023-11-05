package UnitTest.PassingTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.Random.class)
public class TodosTests {

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
    public void getTodosJson() throws JSONException {
        Response res = RestAssured.given().header("Accept",json).get("http://localhost:4567/todos");
        int statusCode = res.getStatusCode();
        String expectedBody ="{\"todos\":[{\"id\":\"2\",\"title\":\"file paperwork\",\"doneStatus\":\"false\",\"description\":\"\",\"tasksof\":[{\"id\":\"1\"}]},{\"id\":\"1\",\"title\":\"scan paperwork\",\"doneStatus\":\"false\",\"description\":\"\",\"categories\":[{\"id\":\"1\"}],\"tasksof\":[{\"id\":\"1\"}]}]}";
        String actualBody = res.getBody().asString();
        JSONAssert.assertEquals(expectedBody,actualBody,false);
        Assertions.assertEquals(200,statusCode);

    }

    @Test
    public void getTodoByIDJSON() throws JSONException {
        Response res = RestAssured.given().header("Accept",json).get("http://localhost:4567/todos/1");
        int statusCode = res.getStatusCode();
        String body = "{\"todos\":[{\"id\":\"1\",\"title\":\"scan paperwork\",\"doneStatus\":\"false\",\"description\":\"\",\"categories\":[{\"id\":\"1\"}],\"tasksof\":[{\"id\":\"1\"}]}]}";
        JSONAssert.assertEquals(body,res.getBody().asString(), false);
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void getTodoByIDXml(){
        Response res = RestAssured.given().header("Accept",xml).get("http://localhost:4567/todos/1");
        int statusCode = res.getStatusCode();
        String body = "<todos><todo><doneStatus>false</doneStatus><description/><tasksof><id>1</id></tasksof><id>1</id><categories><id>1</id></categories><title>scan paperwork</title></todo></todos>";
        Assertions.assertEquals(body,res.getBody().asString());
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void getTodoByInvalidIDJson(){
        Response res = RestAssured.given().header("Accept",json).get("http://localhost:4567/todos/999");
        int statusCode = res.getStatusCode();
        String body = "{\"errorMessages\":[\"Could not find an instance with todos/999\"]}";
        Assertions.assertEquals(body,res.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

    @Test
    public void getTodoByInvalidIDXml(){
        Response res = RestAssured.given().header("Accept",xml).get("http://localhost:4567/todos/999");
        int statusCode = res.getStatusCode();
        String body = "<errorMessages><errorMessage>Could not find an instance with todos/999</errorMessage></errorMessages>";
        Assertions.assertEquals(body,res.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

    @Test
    public void getTaskofTodoJson() throws JSONException{
        Response res = RestAssured.given().header("Accept",json).get("http://localhost:4567/todos/1/tasksof");
        int statusCode = res.getStatusCode();
        String body = "{\"projects\":[{\"id\":\"1\",\"title\":\"Office Work\",\"completed\":\"false\",\"active\":\"false\",\"description\":\"\",\"tasks\":[{\"id\":\"2\"},{\"id\":\"1\"}]}]}";
        JSONAssert.assertEquals(body,res.getBody().asString(), false);
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void getTaskofTodoXml() {
        Response res = RestAssured.given().header("Accept",xml).get("http://localhost:4567/todos/1/tasksof");
        int statusCode = res.getStatusCode();
        String body = "<projects><project><active>false</active><description/><id>1</id><completed>false</completed><title>Office Work</title><tasks><id>2</id></tasks><tasks><id>1</id></tasks></project></projects>";
        Assertions.assertTrue((res.getBody().asString()).matches("<projects>(?:<project>(?:(?!<\\/project>)(?:<active>false<\\/active>|<description\\/>|<id>1<\\/id>|<completed>false<\\/completed>|<title>Office Work<\\/title>|<tasks>(?:(?!<\\/tasks>)(?:<id>2<\\/id>|<id>1<\\/id>))<\\/tasks>)*?)<\\/project>)*<\\/projects>"));
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void headTodos() {
        Response r = RestAssured.given().head("http://localhost:4567/todos");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,200);
    }

    @Test
    public void headID() {
        Response r = RestAssured.given().head("http://localhost:4567/todos/1");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,200);
    }


    @Test
    public void headTaskof(){
        Response r = RestAssured.given().head("http://localhost:4567/todos/1/tasksof");
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,200);
    }

    @Test
    public void postTodoWithoutIDJson(){
        String jsonString = "{" + "\"title\": \"get more paperwork\"," + "\"doneStatus\": false," +
                "\"description\": \"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," +
                "\"categories\": [" + "{" + "\"id\": \"1\"" + "}" + "]" + "}";

        Response res = RestAssured.given()
                       .header("Content-Type",json)
                       .body(jsonString)
                        .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(201,statusCode);
    }

    @Test
    public void postTodoWithoutIDXml(){
        String xmlString = "<todo><title>get more paperwork</title><doneStatus>False</doneStatus></todo>";

        Response res = RestAssured.given()
                .header("Content-Type",xml)
                .body(xmlString)
                .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(201,statusCode);
    }

    @Test
    public void postTodoWithIDJson(){
        String jsonString = "{" + "\"id\": \"4\"," + "\"title\": \"Photocopy Documents\"," + "\"doneStatus\": false," +
                "\"description\": \"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," +
                "\"categories\": [" + "{" + "\"id\": \"1\"" + "}" + "]" + "}";

        Response res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(400,statusCode);
    }

    @Test
    public void postTodoWithIDXml(){
        String xmlString ="<todo><title>get more paperwork</title><doneStatus>False</doneStatus><id>4</id></todo>";

        Response res = RestAssured.given()
                .header("Content-Type",xml)
                .body(xmlString)
                .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(400,statusCode);

    }


    @Test
    public void postTodoWithDiffInfoJson(){
        String jsonString =  "{" + "\"id\": 1," +"\"title\": \"get more paperwork\"," + "\"doneStatus\": false," +
                "\"description\": \"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," +
                "\"categories\": [" + "{" + "\"id\": \"1\"" + "}" + "]" + "}";

        Response res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .post("http://localhost:4567/todos/1");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void postTodoWithDiffInfoXml(){
        String xmlString = "<todo><title>get more paperwork</title><doneStatus>true</doneStatus><id>1</id></todo>";

        Response res = RestAssured.given()
                .header("Content-Type",xml)
                .body(xmlString)
                .post("http://localhost:4567/todos/1");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void postWithoutBodyJson(){
        String jsonString = "{}";

        Response res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(400,statusCode);
    }

    @Test
    public void postWithoutBodyXml(){
        String xmlString = "<>";

        Response res = RestAssured.given()
                .header("Content-Type",xml)
                .body(xmlString)
                .post("http://localhost:4567/todos");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(400,statusCode);
    }

    @Test
    public void putTodoWithDiffInfoJson(){
        String jsonString =  "{" + "\"id\": 1," +"\"title\": \"get more paperwork\"," + "\"doneStatus\": false," +
                "\"description\": \"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," +
                "\"categories\": [" + "{" + "\"id\": \"1\"" + "}" + "]" + "}";

        Response res = RestAssured.given()
                .header("Content-Type",json)
                .body(jsonString)
                .put("http://localhost:4567/todos/1");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void putTodoWithDiffInfoXml(){
        String xmlString = "<todo><title>get more paperwork</title><doneStatus>true</doneStatus><id>1</id></todo>";

        Response res = RestAssured.given()
                .header("Content-Type",xml)
                .body(xmlString)
                .put("http://localhost:4567/todos/1");

        int statusCode = res.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void postTaskofID(){
        Response res = RestAssured.given()
                .header("Content-Type",json)
                .post("http://localhost:4567/todos/1/tasksof");

        int statusCode = res.getStatusCode();
        System.out.println(res.getBody().asString());
        Assertions.assertEquals(201,statusCode);
    }

    @Test
    public void deleteExistingTodo(){

        Response res = RestAssured.delete(" http://localhost:4567/todos/2");
        int statusCode = res.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void deleteNonExistingTodoJson(){
        Response res = RestAssured.given()
                .header("Accept",json)
                .delete("http://localhost:4567/todos/999");
        int statusCode = res.getStatusCode();
        String body = "{\"errorMessages\":[\"Could not find any instances with todos/999\"]}";
        Assertions.assertEquals(body,res.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

    @Test
    public void deleteNonExistingTodoXml(){
        Response res = RestAssured.given()
                .header("Accept",xml)
                .delete("http://localhost:4567/todos/999");
        int statusCode = res.getStatusCode();
        String body = "<errorMessages><errorMessage>Could not find any instances with todos/999</errorMessage></errorMessages>";
        Assertions.assertEquals(body,res.getBody().asString());
        Assertions.assertEquals(404,statusCode);
    }

    @Test
    public void deleteTaskOfTodo(){
        Response res = RestAssured.delete("http://localhost:4567/todos/1/tasksof/1");
        int statusCode = res.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }
}
