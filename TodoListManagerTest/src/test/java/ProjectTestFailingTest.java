import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.Random.class)
public class ProjectTestFailingTest{

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
            Thread.sleep(500);
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





    //----------get project----------

    @Test
    public void getProjectJson() {
        Response r = RestAssured.given().header("Accept", json)
                .get(pUrl);
        int statusCode = r.getStatusCode();

        Assertions.assertEquals(404,statusCode);
    }



    //----------get with filter----------

    @Test
    public void getProjectFilter() {
        String requestBody = "{\n" +
                "    \"completed\": true\n" +
                "}";
        Response r1 = RestAssured.given().header("Accept",json).get(pUrl+"?completed=" + false);

        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(404,statusCode);

        RestAssured.given().header("Accept",json).body(requestBody).post(pUrl+"/1");
        Response r2 = RestAssured.given().header("Accept",json).get(pUrl+"?completed=" + true);
        statusCode = r2.getStatusCode();
        Assertions.assertEquals(404,statusCode);
    }




    //----------head----------

    @Test
    public void getProjectHead() {
        Response r = RestAssured.given().head(pUrl);
        int statusCode = r.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }




    //----------Post with different bodies----------

    @Test
    public void postProjectEmptyJson(){
        String emptyBody = "";
        Response r1 = RestAssured.given().header("Accept",json).body(emptyBody).post(pUrl);
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void postProjectEmptyXml(){
        String emptyBody = "";
        Response r1 = RestAssured.given().header("Content-Type",xml).header("Accept",xml).body(emptyBody).post(pUrl);
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void postProjectNormalJson() {
        String requestBody = "{\n" +
                "    \"title\":\"Test\",\n" +
                "    \"completed\": true,\n" +
                "    \"active\": false,\n" +
                "    \"description\": \"good!\"\n" +
                "}";
        Response r1 = RestAssured.given().header("Accept", json).body(requestBody).post(pUrl);
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 404);

    }

    @Test
    public void postProjectNormalXml() {
        String requestBody = "<todo>" +
                "<title>new title</title>" +
                "<completed>false</completed>" +
                "<active>false</active>" +
                "<description>empty description</description>" +
                "</todo>";
        Response r1 = RestAssured.given().header("Content-Type", xml).header("Accept", xml).body(requestBody).post(pUrl);
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 404);

    }

    @Test
    public void postProjectWithIDJson(){
        String requestBody = "{\n" +
                "    \"id\": 2\n" +
                "}";
        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).post(pUrl);
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }
    @Test
    public void postProjectWithIDXml(){
        String requestBody = "<id>2</id>";
        Response r1 = RestAssured.given().header("Content-Type",xml).header("Accept",xml).body(requestBody).post(pUrl);
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }




    //----------Get with different IDs----------

    @Test
    public void getProjectExistingJson(){
        Response r1 = RestAssured.given().header("Accept", json).get(pUrl + "/1");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 404);

    }

    @Test
    public void getProjectExistingXml() {
        RestAssured.given().header("Accept", xml).body("").post(pUrl);
        Response r1 = RestAssured.given().header("Accept", xml).get(pUrl + "/2");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 404);
    }

    @Test
    public void getProjectNonExistingJson(){
        Response r1 = RestAssured.given().header("Accept",json).get(pUrl+"/0");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }

    @Test
    public void getProjectNonExistingXml(){
        Response r1 = RestAssured.given().header("Accept",xml).get(pUrl+"/0");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }




    //----------Head with IDs----------

    @Test
    public void getProjectExistingIDHead(){
        Response r1 = RestAssured.given().header("Accept",json).head(pUrl+"/1");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void getProjectNoneExistingIDHead(){
        Response r1 = RestAssured.given().header("Accept",json).head(pUrl+"/0");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }





    //----------Post with different IDs----------

    @Test
    public void postProjectExistingIDEmptyJson(){
        RestAssured.given().header("Accept",json).body("").post(pUrl);
        String requestBody = "";
        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).post(pUrl+"/2");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void postProjectExistingIDEmptyXml(){
        RestAssured.given().header("Accept",xml).body("").post(pUrl);
        String requestBody = "";
        Response r1 = RestAssured.given().header("Content-Type",xml).header("Accept",xml).body(requestBody).post(pUrl+"/2");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void postProjectExistingIDJson(){
        RestAssured.given().header("Accept",json).body("").post(pUrl);
        String requestBody = "{\n" +
                "    \"title\":\"Test\",\n" +
                "    \"completed\": true,\n" +
                "    \"active\": false,\n" +
                "    \"description\": \"good!\"\n" +
                "}";
        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).post(pUrl+"/2");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void postProjectExistingIDXml(){
        RestAssured.given().header("Accept",xml).body("").post(pUrl);
        String requestBody = "<todo>" +
                "<title>new title</title>" +
                "<completed>false</completed>" +
                "<active>false</active>" +
                "<description>empty description</description>" +
                "</todo>";;
        Response r1 = RestAssured.given().header("Content-Type",xml).header("Accept",xml).body(requestBody).post(pUrl+"/2");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);

    }

    @Test
    public void postProjectInvalidIDJson(){
        String requestBody = "";
        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).post(pUrl+"/0");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }

    @Test
    public void postProjectInvalidIDXml(){
        String requestBody = "";
        Response r1 = RestAssured.given().header("Content-Type",xml).header("Accept",xml).body(requestBody).post(pUrl+"/0");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }





    //----------Put with different IDs----------

    @Test
    public void putProjectExistingIDEmptyXml(){
        RestAssured.given().header("Accept",xml).body("").post(pUrl);
        String requestBody = "";
        Response r1 = RestAssured.given().header("Content-Type",xml).header("Accept",xml).body(requestBody).put(pUrl+"/2");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void putProjectExistingIDJson(){
        RestAssured.given().header("Accept",json).body("").post(pUrl);
        String requestBody = "{\n" +
                "    \"title\":\"Test\",\n" +
                "    \"completed\": true,\n" +
                "    \"active\": false,\n" +
                "    \"description\": \"good!\"\n" +
                "}";
        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).put(pUrl+"/2");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void putProjectExistingIDXml(){
        RestAssured.given().header("Accept",xml).body("").post(pUrl);
        String requestBody = "<todo>" +
                "<title>new title</title>" +
                "<completed>false</completed>" +
                "<active>false</active>" +
                "<description>empty description</description>" +
                "</todo>";;
        Response r1 = RestAssured.given().header("Content-Type",xml).header("Accept",xml).body(requestBody).put(pUrl+"/2");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);

    }

    @Test
    public void putProjectInvalidIDJson(){
        String requestBody = "";
        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).put(pUrl+"/0");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }

    @Test
    public void putProjectInvalidIDXml(){
        String requestBody = "";
        Response r1 = RestAssured.given().header("Content-Type",xml).header("Accept",xml).body(requestBody).put(pUrl+"/0");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }





    //----------Patch with id?----------

    @Test
    public void patchProject(){
        String requestBody = "";
        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).patch(pUrl+"/1");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,200);
    }




    //----------Delete with different ids----------
    @Test
    public void deleteProjectID(){
        Response r1 = RestAssured.given().header("Accept",json).body("").delete(pUrl+"/1");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);

        Response r2 = RestAssured.given().header("Accept",json).body("").get(pUrl+"/1");
        int statusCode2 = r2.getStatusCode();
        Assertions.assertEquals(statusCode2,200);
    }

    @Test
    public void deleteProjectNonExistingID(){
        Response r1 = RestAssured.given().header("Accept",json).body("").delete(pUrl+"/0");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,200);
    }




    //----------get tasks----------

    @Test
    public void getProjectExistingTask(){

        String requestBody = "{\n" +
                "    \"id\": \"1\"\n" +
                "}";

        RestAssured.given().header("Accept", json).body("").post(pUrl);
        RestAssured.given().header("Accept", json).body(requestBody).post(pUrl + "/2/tasks");
        Response r1 = RestAssured.given().header("Accept", json).body("").get(pUrl + "/2/tasks");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 404);

    }

    @Test
    public void getProjectNoneExistingTask(){

        RestAssured.given().header("Accept",json).body("").post(pUrl);
        Response r1 = RestAssured.given().header("Accept",json).body("").get(pUrl+"/2/tasks");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,404);
    }

    @Test
    public void getProjectTaskNonExistingProject() {
        Response r1 = RestAssured.given().header("Accept", json).body("").get(pUrl + "/0/tasks");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 404);
    }





    //----------add tasks----------

    @Test
    public void postProjectTaskExisting() {
        String requestBody = "{\"id\":\"1\"}";
        RestAssured.given().header("Accept", json).body("").post(pUrl);
        Response r1 = RestAssured.given().header("Accept", json).body(requestBody).post(pUrl + "/2/tasks");
        Response r2 = RestAssured.given().header("Accept", json).body("").get(pUrl + "/2/tasks");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 404);
    }

    @Test
    public void postProjectTaskNonExisting(){
        String requestBody = "{\"id\":\"0\"}";

        Response r1 = RestAssured.given().header("Accept",json).body(requestBody).post(pUrl+"/2/tasks");
        Response r2 = RestAssured.given().header("Accept",json).body("").get(pUrl+"/2/tasks");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode,201);
    }





    //----------delete tasks----------
    @Test
    public void deleteProjectTask(){

        Response r1 = RestAssured.given().header("Accept", json).body("").delete(pUrl + "/1/tasks/1");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 404);
    }


    @Test
    public void deleteProjectTaskNoneExisting() {

        Response r1 = RestAssured.given().header("Accept", json).body("").delete(pUrl + "/1/tasks/0");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 201);
    }





    //----------malformed tests----------

    @Test
    public void postProjectMalformedJson() {
        String requestBody = "{\n" +
                "    \"title\":\"Tt\",\n" +
                "    \"cod\": true,\n" +
                "    \"active\": false,\n" +
                "    \"destion\": \"good!\"\n" +
                "}";
        Response r1 = RestAssured.given().header("Accept", json).body(requestBody).post(pUrl);
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 201);

    }

    @Test
    public void postProjectMalformedXml() {
        RestAssured.given().header("Accept", xml).body("").post(pUrl);
        String requestBody = "<todo>" +
                "<title>n/title>" +
                "<completcompleted>" +
                "<active</active>" +
                "<descript/description>" +
                "</todo>";
        ;
        Response r1 = RestAssured.given().header("Content-Type", xml).header("Accept", xml).body(requestBody).post(pUrl + "/2");
        int statusCode = r1.getStatusCode();
        Assertions.assertEquals(statusCode, 201);

    }

}