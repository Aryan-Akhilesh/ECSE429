package PerformanceTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class TodosPerformanceTest {
    private static final String pUrl = "http://localhost:4567/todos";
    private static ProcessBuilder pb;
    private final int[] populationSize = {1,10,50,100,200,500,1000};
    private OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private String newTodoId;

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
        int max = populationSize[populationSize.length - 1];
        long[] time = new long[populationSize.length];
        double[] cpuUsage = new double[populationSize.length];
        long[] freeMemory = new long[populationSize.length];
        int targetIndex = 0;

        for(int i=1; i<=max;i++){
            JSONObject body = new JSONObject();
            String newTitle = "New Todo " + i;
            String newDescription = "New Description";
            boolean newDoneStatus = false;
            body.put("title",newTitle);
            body.put("doneStatus",newDoneStatus);
            body.put("description",newDescription);
            long startTime = System.nanoTime();
            RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
            long endTime = System.nanoTime();
            if(i == populationSize[targetIndex]){
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize()/(1024L * 1024L);
                time[targetIndex] = (endTime-startTime);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex++;
            }
        }
        System.out.println("---Add TODOS---");
        for (int k = 0; k < populationSize.length; k++) {
            System.out.println("Time for size " + populationSize[k] + ": " + time[k] + "ns");
            System.out.println("CPU usage for size " + populationSize[k] + ": " + cpuUsage[k] + "%");
            System.out.println("Available free memory for size " + populationSize[k] + ": " + freeMemory[k] + "bytes");
        }
    }

    @Test
    public void UpdateTodosJSONPut(){
        int max = populationSize[populationSize.length - 1];
        long[] time = new long[populationSize.length];
        double[] cpuUsage = new double[populationSize.length];
        long[] freeMemory = new long[populationSize.length];
        int targetIndex = 0;

        for(int i=1; i<=max;i++){
            if(i == populationSize[targetIndex]){
                JSONObject body = new JSONObject();
                String newTitle = "New Todo " + i;
                String newDescription = "New Description";
                boolean newDoneStatus = true;
                body.put("title",newTitle);
                body.put("doneStatus",newDoneStatus);
                body.put("description",newDescription);
                long startTime = System.nanoTime();
                RestAssured.given().contentType(ContentType.JSON).body(body.toString()).put(pUrl+"/"+newTodoId);
                long endTime = System.nanoTime();
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize()/(1024L * 1024L);
                time[targetIndex] = (endTime-startTime);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex++;
            }
            JSONObject body = new JSONObject();
            String newTitle = "New Todo " + i;
            String newDescription = "New Description";
            boolean newDoneStatus = false;
            body.put("title",newTitle);
            body.put("doneStatus",newDoneStatus);
            body.put("description",newDescription);
            Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
            newTodoId = response.jsonPath().get("id");
        }
        System.out.println("---Update TODOS---");
        for (int k = 0; k < populationSize.length; k++) {
            System.out.println("Time for size " + populationSize[k] + ": " + time[k] + "ns");
            System.out.println("CPU usage for size " + populationSize[k] + ": " + cpuUsage[k] + "%");
            System.out.println("Available free memory for size " + populationSize[k] + ": " + freeMemory[k] + "bytes");
        }
    }

    @Test
    public void DeleteTodos(){
        int max = populationSize[populationSize.length - 1];
        long[] time = new long[populationSize.length];
        double[] cpuUsage = new double[populationSize.length];
        long[] freeMemory = new long[populationSize.length];
        int targetIndex = 0;

        for(int i=1; i<=max;i++){
            if(i == populationSize[targetIndex]){
                long startTime = System.nanoTime();
                RestAssured.given().delete(pUrl+"/"+newTodoId);
                long endTime = System.nanoTime();
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize()/(1024L * 1024L);
                time[targetIndex] = (endTime-startTime);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex++;
            }
            JSONObject body = new JSONObject();
            String newTitle = "New Todo " + i;
            String newDescription = "New Description";
            boolean newDoneStatus = false;
            body.put("title",newTitle);
            body.put("doneStatus",newDoneStatus);
            body.put("description",newDescription);
            Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
            newTodoId = response.jsonPath().get("id");
        }
        System.out.println("---Delete TODOS---");
        for (int k = 0; k < populationSize.length; k++) {
            System.out.println("Time for size " + populationSize[k] + ": " + time[k] + "ns");
            System.out.println("CPU usage for size " + populationSize[k] + ": " + cpuUsage[k] + "%");
            System.out.println("Available free memory for size " + populationSize[k] + ": " + freeMemory[k] + "bytes");
        }
    }

}
