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
    private Response response;
    private String newCategoryId;

    OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    private final int[] targetSize = {1, 10, 50, 100, 250, 500, 1000};

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
    public void addCategoryPerformance() {
        System.out.println("---Add Category---");
        int targetIndex = 0;
        for (int i = 1; i <= targetSize[targetSize.length - 1]; i++) {
            if (targetSize[targetIndex] == i) {
                long start = System.nanoTime();
                RestAssured.given().header("Content-Type", json).body("{\"title\":\"" + i + "\",\"description\":\"\"}").post("http://localhost:4567/categories");
                long finish = System.nanoTime();
                long timeElapsed = finish - start;
                double elapsedTimeInSecond = (double) timeElapsed / 1_000_000_000;
                double cpuUsage = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize()/ (1024L * 1024L);
                System.out.println("------SIZE " + i + "------");
                System.out.println("Free memory size for size " + i + " categories: " + memory + " MB");
                System.out.println("CPU usage for size " + i + " categories: " + cpuUsage + "%");
                System.out.println("Time taken for size " + i + " categories: " + elapsedTimeInSecond + " seconds");
                targetIndex++;
            } else {
                RestAssured.given().header("Content-Type", json).body("{\"title\":\"" + i + "\",\"description\":\"\"}").post("http://localhost:4567/categories");
            }
        }
    }

    @Test
    public void deleteCategoryPerformance() {
        System.out.println("---Delete Category---");
        int targetIndex = 0;
        for (int i = 1; i <= targetSize[targetSize.length - 1]; i++) {
            if (targetSize[targetIndex] == i) {
                long start = System.nanoTime();
                RestAssured.given().delete("http://localhost:4567/categories/" + newCategoryId);
                long finish = System.nanoTime();
                long timeElapsed = finish - start;
                double elapsedTimeInSecond = (double) timeElapsed / 1_000_000_000;
                double cpuUsage = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize()/ (1024L * 1024L);
                System.out.println("------SIZE " + i + "------");
                System.out.println("Free memory size for size " + i + " categories: " + memory + " MB");
                System.out.println("CPU usage for size " + i + " categories: " + cpuUsage + "%");
                System.out.println("Time taken for size " + i + " categories: " + elapsedTimeInSecond + " seconds");
                targetIndex++;
            }
            response = RestAssured.given().header("Content-Type", json).body("{\"title\":\"" + i + "\",\"description\":\"\"}").post("http://localhost:4567/categories");
            newCategoryId = response.jsonPath().get("id");
        }
    }

    @Test
    public void changeCategoryPerformance() {
        System.out.println("---Change Category---");
        int targetIndex = 0;
        for (int i = 1; i <= targetSize[targetSize.length - 1]; i++) {
            if (targetSize[targetIndex] == i) {
                long start = System.nanoTime();
                RestAssured.given().header("Content-Type", json).body("{\"title\": \"new category\"}").put("http://localhost:4567/categories/" + newCategoryId);
                long finish = System.nanoTime();
                long timeElapsed = finish - start;
                double elapsedTimeInSecond = (double) timeElapsed / 1_000_000_000;
                double cpuUsage = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize()/ (1024L * 1024L);
                System.out.println("------SIZE " + i + "------");
                System.out.println("Free memory size for size " + i + " categories: " + memory + " MB");
                System.out.println("CPU usage for size " + i + " categories: " + cpuUsage + "%");
                System.out.println("Time taken for size " + i + " categories: " + elapsedTimeInSecond + " seconds");
                targetIndex++;
            }
            response = RestAssured.given().header("Content-Type", json).body("{\"title\":\"" + i + "\",\"description\":\"\"}").post("http://localhost:4567/categories");
            newCategoryId = response.jsonPath().get("id");
        }
    }
}
