package PerformanceTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.XML;
import org.junit.jupiter.api.*;

import javax.xml.crypto.dsig.XMLObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodosPerformanceTest {
    private static final String pUrl = "http://localhost:4567/todos";
    private static ProcessBuilder pb;

    private final String json = "application/json";
    private final String xml = "application/xml";

    private final int[] populationSize = {1,10,50,100,200,500,1000};

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
    public void CreateTodosJSONPost(){
        List<Long> timeTable = new ArrayList<Long>();
        for(int amount : populationSize){
            long start = System.nanoTime();
            for(int i=0;i<amount;i++){
                JSONObject body = new JSONObject();
                String newTitle = "New Todo " + i;
                String newDescription = "New Description";
                boolean newDoneStatus = false;
                body.put("title",newTitle);
                body.put("doneStatus",newDoneStatus);
                body.put("description",newDescription);
                Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
            }
            long end = System.nanoTime();
            long time = end-start;
            timeTable.add(time);
        }
        System.out.println(timeTable);
    }

    @Test
    public void CreateTodosXMLPost(){
        List<Long> timeTable = new ArrayList<Long>();
        for(int amount : populationSize){
            long start = System.nanoTime();
            for(int i=0;i<amount;i++){
                JSONObject body = new JSONObject();
                String newTitle = "New Todo " + i;
                String newDescription = "New Description";
                String newDoneStatus = "false";
                body.put("title",newTitle);
                body.put("doneStatus",newDoneStatus);
                body.put("description",newDescription);
                String xmlBody = XML.toString(body,"todo");
                Response response = RestAssured.given().contentType(ContentType.XML).body(xmlBody).post(pUrl);
            }
            long end = System.nanoTime();
            long time = end-start;
            timeTable.add(time);
        }
        System.out.println(timeTable);
    }

    @Test
    public void UpdateTodosJSONPost(){
        List<Long> timeTable = new ArrayList<Long>();
        for(int amount : populationSize){
            for(int i=0;i<amount;i++){
                JSONObject body = new JSONObject();
                String newTitle = "New Todo " + i;
                String newDescription = "New Description";
                boolean newDoneStatus = false;
                body.put("title",newTitle);
                body.put("doneStatus",newDoneStatus);
                body.put("description",newDescription);
                Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
            }
            long start = System.nanoTime();
            for(int i=1;i<amount;i++){
                JSONObject body = new JSONObject();
                String newTitle = "New Todo " + i;
                String newDescription = "New Description";
                boolean newDoneStatus = true;
                body.put("title",newTitle);
                body.put("doneStatus",newDoneStatus);
                body.put("description",newDescription);
                Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl+"/"+i);
            }
            long end = System.nanoTime();
            long time = end-start;
            timeTable.add(time);
        }
        System.out.println(timeTable);
    }

    @Test
    public void UpdateTodosJSONPut(){
        List<Long> timeTable = new ArrayList<Long>();
        for(int amount : populationSize){
            for(int i=0;i<amount;i++){
                JSONObject body = new JSONObject();
                String newTitle = "New Todo " + i;
                String newDescription = "New Description";
                boolean newDoneStatus = false;
                body.put("title",newTitle);
                body.put("doneStatus",newDoneStatus);
                body.put("description",newDescription);
                Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
            }
            long start = System.nanoTime();
            for(int i=1;i<amount;i++){
                JSONObject body = new JSONObject();
                String newTitle = "New Todo " + i;
                String newDescription = "New Description";
                boolean newDoneStatus = true;
                body.put("title",newTitle);
                body.put("doneStatus",newDoneStatus);
                body.put("description",newDescription);
                Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).put(pUrl+"/"+i);
            }
            long end = System.nanoTime();
            long time = end-start;
            timeTable.add(time);
        }
        System.out.println(timeTable);
    }

    @Test
    public void DeleteTodos(){
        List<Long> timeTable = new ArrayList<Long>();
        for(int amount : populationSize){
            for(int i=0;i<amount;i++){
                JSONObject body = new JSONObject();
                String newTitle = "New Todo " + i;
                String newDescription = "New Description";
                boolean newDoneStatus = false;
                body.put("title",newTitle);
                body.put("doneStatus",newDoneStatus);
                body.put("description",newDescription);
                Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
            }
            long start = System.nanoTime();
            for(int i=1;i<amount;i++){
                Response response = RestAssured.given().delete(pUrl+"/"+i);
            }
            long end = System.nanoTime();
            long time = end-start;
            timeTable.add(time);
        }
        System.out.println(timeTable);
    }

}
