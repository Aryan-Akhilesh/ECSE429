package PerformanceTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.Random.class)
public class CategoriesPerformanceTest {
    private static ProcessBuilder pb;

    private final String json = "application/json";
    private final String xml = "application/xml";
    private Response response;
    private String newCategoryId;

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

    // add --> post
    // add when there is not a lot of items
    // add when there is ton of items

    @Test
    public void addCategoryPerformance() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        for (int i = 0; i < 10000; i++) {
            if (i == 1 || i == 1000 || i == 2500 || i == 5000 || i == 9999) {
                long start = System.nanoTime();
                Response r = RestAssured.given().header("Content-Type", json).body("{\"title\": \"new category\"}").post("http://localhost:4567/categories");
                long finish = System.nanoTime();
                long timeElapsed = finish - start;
                double elapsedTimeInSecond = (double) timeElapsed / 1_000_000_000;
                double cpuUsage = osBean.getCpuLoad() * 100;
                System.out.println("CPU Usage during add at " + i + " categories: " + cpuUsage + "%");
                System.out.println("At " + i + " categories, the time taken to add is: " + elapsedTimeInSecond + " seconds");
            } else {
                RestAssured.given().header("Content-Type", json).body("{\"title\":\"" + i + "\",\"description\":\"\"}").post("http://localhost:4567/categories");
            }
        }
    }

    // delete --> delete
    @Test
    public void deleteCategoryPerformance() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        for (int i = 0; i < 10000; i++) {
            if (i == 1 || i == 1000 || i == 2500 || i == 5000 | i == 9999) {
                long start = System.nanoTime();
                RestAssured.given().delete("http://localhost:4567/categories/" + newCategoryId);
                long finish = System.nanoTime();
                long timeElapsed = finish - start;
                double elapsedTimeInSecond = (double) timeElapsed / 1_000_000_000;
                double cpuUsage = osBean.getCpuLoad() * 100;
                System.out.println("CPU Usage during delete at " + i + " categories: " + cpuUsage + "%");
                System.out.println("At " + i + " categories, the time taken to delete is: " + elapsedTimeInSecond + " seconds");
            }
            response = RestAssured.given().header("Content-Type", json).body("{\"title\":\"" + i + "\",\"description\":\"\"}").post("http://localhost:4567/categories");
            newCategoryId = response.jsonPath().get("id");
        }
    }

    @Test
    public void changeCategoryPerformance() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        for (int i = 0; i < 10000; i++) {
            if (i == 1 || i == 1000 || i == 2500 || i == 5000 | i == 9999) {
                long start = System.nanoTime();
                RestAssured.given().header("Content-Type", json).body("{\"title\": \"new category\"}").put("http://localhost:4567/categories/" + newCategoryId);
                long finish = System.nanoTime();
                long timeElapsed = finish - start;
                double elapsedTimeInSecond = (double) timeElapsed / 1_000_000_000;
                double cpuUsage = osBean.getCpuLoad() * 100;
                System.out.println("CPU Usage during change at " + i + " categories: " + cpuUsage + "%");
                System.out.println("At " + i + " categories, the time taken to change is: " + elapsedTimeInSecond + " seconds");
            }
            response = RestAssured.given().header("Content-Type", json).body("{\"title\":\"" + i + "\",\"description\":\"\"}").post("http://localhost:4567/categories");
            newCategoryId = response.jsonPath().get("id");
        }

    }
}
