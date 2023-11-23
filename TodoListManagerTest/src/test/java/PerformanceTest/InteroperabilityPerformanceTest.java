package PerformanceTest;

import com.sun.management.OperatingSystemMXBean;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class InteroperabilityPerformanceTest {

    private static ProcessBuilder pb;

    private final String json = "application/json";
    private final String xml = "application/xml";

    private final int[] targetSize = {1,10,20,50,100,200,500,1000};

    private OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();;

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
    public void AddCategoriesToTodoJson() {
        String todoId;
        String[] categoryId = new String[targetSize[targetSize.length-1]];
        long[] time = new long[targetSize.length];
        double[] cpuUsage = new double[targetSize.length];
        long[] freeMemory = new long[targetSize.length];

        // Get a new todoitem
        Response response = RestAssured.given().body("{\"title\":\"todo\"}").post("http://localhost:4567/todos");
        todoId = response.jsonPath().get("id");

        // Generate 1000 categories, store the id
        for(int i = 0; i < 1000; i++) {
            String body = "{\"title\":\"Test\",\"description\":\"\"}";
            response = RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post("http://localhost:4567/categories");
            categoryId[i] = response.jsonPath().get("id");
        }

        // Add categories to todoitem
        String url = "http://localhost:4567/todos/" + todoId + "/categories";
        int targetIndex = 0;
        for (int j = 1; j <= 1000; j++) {
            long startTime = System.nanoTime();
            String body = "{\"id\":\"" + categoryId[j-1] + "\"}";
            RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post(url);
            long endTime = System.nanoTime();
            if (j == targetSize[targetIndex]) {
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize();
                time[targetIndex] = (endTime-startTime);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex++;
            }
        }

        // Printing results
        System.out.println("---Add categories to a todo---");
        for (int k = 0; k < targetSize.length; k++) {
            System.out.println("Time for size " + targetSize[k] + ": " + time[k] + "ns");
            System.out.println("CPU usage for size " + targetSize[k] + ": " + cpuUsage[k] + "%");
            System.out.println("Available free memory for size" + targetSize[k] + ": " + freeMemory[k] + "bytes");
        }
    }
}
