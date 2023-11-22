package PerformanceTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.XML;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.xml.crypto.dsig.XMLObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectsPerformanceTest {
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

    @Test
    public void createProjectsJsonPost(){
        List<Long> timeTable = new ArrayList<Long>();
        for(int i=0;i<1000;i++){
            JSONObject body = new JSONObject();
            String newTitle = "New project " + String.valueOf(i);
            Boolean newCompleted = false;
            Boolean newActive = false;
            String newDescription = "New Description";
            body.put("title",newTitle);
            body.put("completed",newCompleted);
            body.put("active",newActive);
            body.put("description",newDescription);
            long start = System.nanoTime();
            Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
            long end = System.nanoTime();
            long time = end-start;
            timeTable.add(time);
        }
        System.out.println(timeTable);
    }

    @Test
    public void createProjectsJsonPut(){
        List<Long> timeTable = new ArrayList<Long>();
        for(int i=0;i<1000;i++){
            JSONObject body = new JSONObject();
            String newTitle = "New project " + String.valueOf(i);
            Boolean newCompleted = false;
            Boolean newActive = false;
            String newDescription = "New Description";
            body.put("title",newTitle);
            body.put("completed",newCompleted);
            body.put("active",newActive);
            body.put("description",newDescription);
            long start = System.nanoTime();
            Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).put(pUrl);
            long end = System.nanoTime();
            long time = end-start;
            timeTable.add(time);
        }
        System.out.println(timeTable);
    }

    @Test
    public void createProjectsXmlPost(){
        List<Long> timeTable = new ArrayList<Long>();
        for(int i=0;i<1000;i++){
            JSONObject body = new JSONObject();
            String newTitle = "New project " + String.valueOf(i);
            Boolean newCompleted = false;
            Boolean newActive = false;
            String newDescription = "New Description";
            body.put("title",newTitle);
            body.put("completed",newCompleted);
            body.put("active",newActive);
            body.put("description",newDescription);
            String xmlBody = XML.toString(body);
            long start = System.nanoTime();
            Response response = RestAssured.given().contentType(ContentType.XML).body(xmlBody).post(pUrl);
            long end = System.nanoTime();
            long time = end-start;
            timeTable.add(time);
        }
        System.out.println(timeTable);
    }

    @Test
    public void createProjectsXmlPut(){
        List<Long> timeTable = new ArrayList<Long>();
        for(int i=0;i<1000;i++){
            JSONObject body = new JSONObject();
            String newTitle = "New project " + String.valueOf(i);
            Boolean newCompleted = false;
            Boolean newActive = false;
            String newDescription = "New Description";
            body.put("title",newTitle);
            body.put("completed",newCompleted);
            body.put("active",newActive);
            body.put("description",newDescription);
            String xmlBody = XML.toString(body);
            long start = System.nanoTime();
            Response response = RestAssured.given().contentType(ContentType.XML).body(xmlBody).put(pUrl);
            long end = System.nanoTime();
            long time = end-start;
            timeTable.add(time);
        }
        System.out.println(timeTable);
    }


}
