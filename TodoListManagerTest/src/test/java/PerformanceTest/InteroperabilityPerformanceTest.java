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

    private final int[] targetSize = {1,10,50,100,200,500,1000};

    private final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

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
    public void AddCategoriesToTodo() {
        long startSampleTime = System.nanoTime();
        double[] sampleTimeStore = new double[targetSize.length];
        int max = targetSize[targetSize.length-1];
        String todoId;
        String[] categoryId = new String[max];
        double[] time = new double[targetSize.length];
        double[] cpuUsage = new double[targetSize.length];
        long[] freeMemory = new long[targetSize.length];

        // Get a new todoitem
        Response response = RestAssured.given().body("{\"title\":\"todo\"}").post("http://localhost:4567/todos");
        todoId = response.jsonPath().get("id");

        // Generate categories, store the id
        for(int i = 0; i < max; i++) {
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
        for (int j = 1; j <= max; j++) {
            long startTime = System.nanoTime();
            String body = "{\"id\":\"" + categoryId[j-1] + "\"}";
            RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post(url);
            long endTime = System.nanoTime();
            if (j == targetSize[targetIndex]) {
                long sampleTimeElapsed = endTime - startSampleTime;
                double sampleTimeElapsedInSeconds = (double) sampleTimeElapsed / 1_000_000_000;
                sampleTimeStore[targetIndex] = sampleTimeElapsedInSeconds;
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize() / (1024*1024);
                time[targetIndex] = (endTime-startTime) / (1000000000f);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex++;
            }
        }

        // Printing results
        System.out.println("---Add categories to a todo---");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)", "Sample Time (s)");
        for (int i = 0; i < targetSize.length; i++) {
            System.out.printf("%-10d %-20f %-20f %-20d %-20f%n",
                    targetSize[i],
                    time[i],
                    cpuUsage[i],
                    freeMemory[i],
                    sampleTimeStore[i]);
        }
        System.out.println("------------------------------");
    }

    @Test
    public void DeleteCategoriesFromTodo() {
        long startSampleTime = System.nanoTime();
        double[] sampleTimeStore = new double[targetSize.length];
        int max = targetSize[targetSize.length-1];
        String todoId;
        String[] categoryId = new String[max];
        double[] time = new double[targetSize.length];
        double[] cpuUsage = new double[targetSize.length];
        long[] freeMemory = new long[targetSize.length];

        // Get a new todoitem
        Response response = RestAssured.given().body("{\"title\":\"todo\"}").post("http://localhost:4567/todos");
        todoId = response.jsonPath().get("id");

        // Generate categories, store the id
        for(int i = 0; i < max; i++) {
            String body = "{\"title\":\"Test\",\"description\":\"\"}";
            response = RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post("http://localhost:4567/categories");
            categoryId[i] = response.jsonPath().get("id");
        }

        // Add categories to todoitem
        String url = "http://localhost:4567/todos/" + todoId + "/categories";
        for (int j = 1; j <= max; j++) {
            String body = "{\"id\":\"" + categoryId[j-1] + "\"}";
            RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post(url);
        }

        // Delete categories from todoitem
        int targetIndex = targetSize.length-1;
        for (int j = max; j > 0; j--) {
            long startTime = System.nanoTime();
            RestAssured.given().delete(url + "/" + categoryId[j-1]);
            long endTime = System.nanoTime();
            if (j == targetSize[targetIndex]) {
                long sampleTimeElapsed = endTime - startSampleTime;
                double sampleTimeElapsedInSeconds = (double) sampleTimeElapsed / 1_000_000_000;
                sampleTimeStore[targetIndex] = sampleTimeElapsedInSeconds;
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize() / (1024*1024);
                time[targetIndex] = (endTime-startTime) / (1000000000f);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex--;
            }
        }

        // Printing results
        System.out.println("---Delete categories from a todo---");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)", "Sample Time (s)");
        for (int i = 0; i < targetSize.length; i++) {
            System.out.printf("%-10d %-20f %-20f %-20d %-20f%n",
                    targetSize[i],
                    time[i],
                    cpuUsage[i],
                    freeMemory[i],
                    sampleTimeStore[i]);
        }
        System.out.println("-----------------------------------");
    }

    @Test
    public void AddCategoriesToProject() {
        long startSampleTime = System.nanoTime();
        double[] sampleTimeStore = new double[targetSize.length];
        int max = targetSize[targetSize.length-1];
        String projectId;
        String[] categoryId = new String[max];
        double[] time = new double[targetSize.length];
        double[] cpuUsage = new double[targetSize.length];
        long[] freeMemory = new long[targetSize.length];

        // Get a new project
        Response response = RestAssured.given().body("{\"title\":\"project\"}").post("http://localhost:4567/projects");
        projectId = response.jsonPath().get("id");

        // Generate categories, store the id
        for(int i = 0; i < max; i++) {
            String body = "{\"title\":\"Test\",\"description\":\"\"}";
            response = RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post("http://localhost:4567/categories");
            categoryId[i] = response.jsonPath().get("id");
        }

        // Add categories to project
        String url = "http://localhost:4567/projects/" + projectId + "/categories";
        int targetIndex = 0;
        for (int j = 1; j <= max; j++) {
            long startTime = System.nanoTime();
            String body = "{\"id\":\"" + categoryId[j-1] + "\"}";
            RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post(url);
            long endTime = System.nanoTime();
            if (j == targetSize[targetIndex]) {
                long sampleTimeElapsed = endTime - startSampleTime;
                double sampleTimeElapsedInSeconds = (double) sampleTimeElapsed / 1_000_000_000;
                sampleTimeStore[targetIndex] = sampleTimeElapsedInSeconds;
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize() / (1024*1024);
                time[targetIndex] = (endTime-startTime) / (1000000000f);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex++;
            }
        }

        // Printing results
        System.out.println("---Add categories to a project---");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)", "Sample Time (s)");
        for (int i = 0; i < targetSize.length; i++) {
            System.out.printf("%-10d %-20f %-20f %-20d %-20f%n",
                    targetSize[i],
                    time[i],
                    cpuUsage[i],
                    freeMemory[i],
                    sampleTimeStore[i]);
        }
        System.out.println("---------------------------------");
    }

    @Test
    public void DeleteCategoriesFromProject() {
        long startSampleTime = System.nanoTime();
        double[] sampleTimeStore = new double[targetSize.length];
        int max = targetSize[targetSize.length-1];
        String projectId;
        String[] categoryId = new String[max];
        double[] time = new double[targetSize.length];
        double[] cpuUsage = new double[targetSize.length];
        long[] freeMemory = new long[targetSize.length];

        // Get a new project
        Response response = RestAssured.given().body("{\"title\":\"project\"}").post("http://localhost:4567/projects");
        projectId = response.jsonPath().get("id");

        // Generate categories, store the id
        for(int i = 0; i < max; i++) {
            String body = "{\"title\":\"Test\",\"description\":\"\"}";
            response = RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post("http://localhost:4567/categories");
            categoryId[i] = response.jsonPath().get("id");
        }

        // Add categories to project
        String url = "http://localhost:4567/projects/" + projectId + "/categories";
        for (int j = 1; j <= max; j++) {
            String body = "{\"id\":\"" + categoryId[j-1] + "\"}";
            RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post(url);
        }

        // Delete categories from project
        int targetIndex = targetSize.length-1;
        for (int j = max; j > 0; j--) {
            long startTime = System.nanoTime();
            RestAssured.given().delete(url + "/" + categoryId[j-1]);
            long endTime = System.nanoTime();
            if (j == targetSize[targetIndex]) {
                long sampleTimeElapsed = endTime - startSampleTime;
                double sampleTimeElapsedInSeconds = (double) sampleTimeElapsed / 1_000_000_000;
                sampleTimeStore[targetIndex] = sampleTimeElapsedInSeconds;
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize() / (1024*1024);
                time[targetIndex] = (endTime-startTime) / (1000000000f);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex--;
            }
        }

        // Printing results
        System.out.println("---Delete categories from a project---");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)", "Sample Time (s)");
        for (int i = 0; i < targetSize.length; i++) {
            System.out.printf("%-10d %-20f %-20f %-20d %-20f%n",
                    targetSize[i],
                    time[i],
                    cpuUsage[i],
                    freeMemory[i],
                    sampleTimeStore[i]);
        }
        System.out.println("--------------------------------------");
    }

    @Test
    public void AddTodosToCategory() {
        long startSampleTime = System.nanoTime();
        double[] sampleTimeStore = new double[targetSize.length];
        int max = targetSize[targetSize.length-1];
        String categoryId;
        String[] todoId = new String[max];
        double[] time = new double[targetSize.length];
        double[] cpuUsage = new double[targetSize.length];
        long[] freeMemory = new long[targetSize.length];

        // Get a new category
        Response response = RestAssured.given().body("{\"title\":\"category\"}").post("http://localhost:4567/categories");
        categoryId = response.jsonPath().get("id");

        // Generate todos, store the id
        for(int i = 0; i < max; i++) {
            String body = "{\"title\":\"Test\",\"description\":\"\"}";
            response = RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post("http://localhost:4567/todos");
            todoId[i] = response.jsonPath().get("id");
        }

        // Add todos to category
        String url = "http://localhost:4567/categories/" + categoryId + "/todos";
        int targetIndex = 0;
        for (int j = 1; j <= max; j++) {
            long startTime = System.nanoTime();
            String body = "{\"id\":\"" + todoId[j-1] + "\"}";
            RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post(url);
            long endTime = System.nanoTime();
            if (j == targetSize[targetIndex]) {
                long sampleTimeElapsed = endTime - startSampleTime;
                double sampleTimeElapsedInSeconds = (double) sampleTimeElapsed / 1_000_000_000;
                sampleTimeStore[targetIndex] = sampleTimeElapsedInSeconds;
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize() / (1024*1024);
                time[targetIndex] = (endTime-startTime) / (1000000000f);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex++;
            }
        }

        // Printing results
        System.out.println("---Add todos to a category---");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)", "Sample Time (s)");
        for (int i = 0; i < targetSize.length; i++) {
            System.out.printf("%-10d %-20f %-20f %-20d %-20f%n",
                    targetSize[i],
                    time[i],
                    cpuUsage[i],
                    freeMemory[i],
                    sampleTimeStore[i]);
        }
        System.out.println("-----------------------------");
    }

    @Test
    public void DeleteTodosFromCategory() {
        long startSampleTime = System.nanoTime();
        double[] sampleTimeStore = new double[targetSize.length];
        int max = targetSize[targetSize.length-1];
        String categoryId;
        String[] todoId = new String[max];
        double[] time = new double[targetSize.length];
        double[] cpuUsage = new double[targetSize.length];
        long[] freeMemory = new long[targetSize.length];

        // Get a new category
        Response response = RestAssured.given().body("{\"title\":\"category\"}").post("http://localhost:4567/categories");
        categoryId = response.jsonPath().get("id");

        // Generate todos, store the id
        for(int i = 0; i < max; i++) {
            String body = "{\"title\":\"Test\",\"description\":\"\"}";
            response = RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post("http://localhost:4567/todos");
            todoId[i] = response.jsonPath().get("id");
        }

        // Add todos to category
        String url = "http://localhost:4567/categories/" + categoryId + "/todos";
        for (int j = 1; j <= max; j++) {
            String body = "{\"id\":\"" + todoId[j-1] + "\"}";
            RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post(url);
        }

        // Delete todos from category
        int targetIndex = targetSize.length-1;
        for (int j = max; j > 0; j--) {
            long startTime = System.nanoTime();
            RestAssured.given().delete(url + "/" + todoId[j-1]);
            long endTime = System.nanoTime();
            if (j == targetSize[targetIndex]) {
                long sampleTimeElapsed = endTime - startSampleTime;
                double sampleTimeElapsedInSeconds = (double) sampleTimeElapsed / 1_000_000_000;
                sampleTimeStore[targetIndex] = sampleTimeElapsedInSeconds;
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize() / (1024*1024);
                time[targetIndex] = (endTime-startTime) / (1000000000f);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex--;
            }
        }

        // Printing results
        System.out.println("---Delete todos from a category---");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)", "Sample Time (s)");
        for (int i = 0; i < targetSize.length; i++) {
            System.out.printf("%-10d %-20f %-20f %-20d %-20f%n",
                    targetSize[i],
                    time[i],
                    cpuUsage[i],
                    freeMemory[i],
                    sampleTimeStore[i]);
        }
        System.out.println("----------------------------------");
    }

    @Test
    public void AddProjectsToCategory() {
        long startSampleTime = System.nanoTime();
        double[] sampleTimeStore = new double[targetSize.length];
        int max = targetSize[targetSize.length-1];
        String categoryId;
        String[] projectId = new String[max];
        double[] time = new double[targetSize.length];
        double[] cpuUsage = new double[targetSize.length];
        long[] freeMemory = new long[targetSize.length];

        // Get a new category
        Response response = RestAssured.given().body("{\"title\":\"category\"}").post("http://localhost:4567/categories");
        categoryId = response.jsonPath().get("id");

        // Generate projects, store the id
        for(int i = 0; i < max; i++) {
            String body = "{\"title\":\"Test\",\"description\":\"\"}";
            response = RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post("http://localhost:4567/projects");
            projectId[i] = response.jsonPath().get("id");
        }

        // Add projects to category
        String url = "http://localhost:4567/categories/" + categoryId + "/todos";
        int targetIndex = 0;
        for (int j = 1; j <= max; j++) {
            long startTime = System.nanoTime();
            String body = "{\"id\":\"" + projectId[j-1] + "\"}";
            RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post(url);
            long endTime = System.nanoTime();
            if (j == targetSize[targetIndex]) {
                long sampleTimeElapsed = endTime - startSampleTime;
                double sampleTimeElapsedInSeconds = (double) sampleTimeElapsed / 1_000_000_000;
                sampleTimeStore[targetIndex] = sampleTimeElapsedInSeconds;
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize() / (1024*1024);
                time[targetIndex] = (endTime-startTime) / (1000000000f);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex++;
            }
        }

        // Printing results
        System.out.println("---Add projects to a category---");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)", "Sample Time (s)");
        for (int i = 0; i < targetSize.length; i++) {
            System.out.printf("%-10d %-20f %-20f %-20d %-20f%n",
                    targetSize[i],
                    time[i],
                    cpuUsage[i],
                    freeMemory[i],
                    sampleTimeStore[i]);
        }
        System.out.println("--------------------------------");
    }

    @Test
    public void DeleteProjectsFromCategory() {
        long startSampleTime = System.nanoTime();
        double[] sampleTimeStore = new double[targetSize.length];
        int max = targetSize[targetSize.length-1];
        String categoryId;
        String[] projectId = new String[max];
        double[] time = new double[targetSize.length];
        double[] cpuUsage = new double[targetSize.length];
        long[] freeMemory = new long[targetSize.length];

        // Get a new category
        Response response = RestAssured.given().body("{\"title\":\"category\"}").post("http://localhost:4567/categories");
        categoryId = response.jsonPath().get("id");

        // Generate projects, store the id
        for(int i = 0; i < max; i++) {
            String body = "{\"title\":\"Test\",\"description\":\"\"}";
            response = RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post("http://localhost:4567/projects");
            projectId[i] = response.jsonPath().get("id");
        }

        // Add projects to category
        String url = "http://localhost:4567/categories/" + categoryId + "/projects";
        for (int j = 1; j <= max; j++) {
            String body = "{\"id\":\"" + projectId[j-1] + "\"}";
            RestAssured.given()
                    .header("Accept", json)
                    .body(body)
                    .post(url);
        }

        // Delete projects from category
        int targetIndex = targetSize.length-1;
        for (int j = max; j > 0; j--) {
            long startTime = System.nanoTime();
            RestAssured.given().delete(url + "/" + projectId[j-1]);
            long endTime = System.nanoTime();
            if (j == targetSize[targetIndex]) {
                long sampleTimeElapsed = endTime - startSampleTime;
                double sampleTimeElapsedInSeconds = (double) sampleTimeElapsed / 1_000_000_000;
                sampleTimeStore[targetIndex] = sampleTimeElapsedInSeconds;
                double cpuLoad = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize() / (1024*1024);
                time[targetIndex] = (endTime-startTime) / (1000000000f);
                cpuUsage[targetIndex] = cpuLoad;
                freeMemory[targetIndex] = memory;
                targetIndex--;
            }
        }

        // Printing results
        System.out.println("---Delete projects from a category---");
        System.out.printf("%-10s %-20s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)", "Sample Time (s)");
        for (int i = 0; i < targetSize.length; i++) {
            System.out.printf("%-10d %-20f %-20f %-20d %-20f%n",
                    targetSize[i],
                    time[i],
                    cpuUsage[i],
                    freeMemory[i],
                    sampleTimeStore[i]);
        }
        System.out.println("-------------------------------------");
    }
}
