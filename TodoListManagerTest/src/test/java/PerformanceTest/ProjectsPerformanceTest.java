package PerformanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
@TestMethodOrder(MethodOrderer.Random.class)
public class ProjectsPerformanceTest {
    private static final String pUrl = "http://localhost:4567/projects";
    private static ProcessBuilder pb;

    private final String json = "application/json";
    private final String xml = "application/xml";

    private final int[] target = {1,5,10,50,100,200,500,1000};
    private final int tot = target[target.length-1];

    OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

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
    public void createProjectsJsonPut() {
        double[] timeStore = new double[target.length];
        double[] cpuUsageStore = new double[target.length];
        long[] freeMemoryStore = new long[target.length];
        int index = 0;
        for (int i = 0; i < tot; i++) {
            JSONObject body = new JSONObject();
            String newTitle = "New project " + i;
            Boolean newCompleted = false;
            Boolean newActive = false;
            String newDescription = "New Description";
            body.put("title", newTitle);
            body.put("completed", newCompleted);
            body.put("active", newActive);
            body.put("description", newDescription);
            long start = System.nanoTime();
            Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).put(pUrl);
            long end = System.nanoTime();
            long time = end - start;
            double time_in_second = (double) time / 1_000_000_000;
            double cpuUsage = osBean.getCpuLoad() * 100;
            long memory = osBean.getFreeMemorySize()/ (1024L * 1024L);
            if(i == target[index]-1){
                timeStore[index] = time_in_second;
                cpuUsageStore[index] = cpuUsage;
                freeMemoryStore[index] = memory;
                index++;
            }
        }
        System.out.println("---------Add Project Statistics---------");
        System.out.printf("%-10s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)");
        for (int i = 0; i < target.length; i++) {
            System.out.printf("%-10d %-20.8f %-20.4f %-20d%n",
                    target[i],
                    timeStore[i],
                    cpuUsageStore[i],
                    freeMemoryStore[i]);
        }
        System.out.println("----------------------------------------");
    }

    @Test
    public void amendProjectsJsonPost(){
        double[] timeStore = new double[target.length];
        double[] cpuUsageStore = new double[target.length];
        long[] freeMemoryStore = new long[target.length];

        for(int i=0;i<tot;i++){
            JSONObject body = new JSONObject();
            String newTitle = "New project " + i;
            Boolean newCompleted = false;
            Boolean newActive = false;
            String newDescription = "New Description";
            body.put("title",newTitle);
            body.put("completed",newCompleted);
            body.put("active",newActive);
            body.put("description",newDescription);
            Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
        }

        int index = 0;

        for(int i=0;i<tot;i++){
            JSONObject body = new JSONObject();
            String newTitle = "modified project ";
            Boolean newCompleted = true;
            Boolean newActive = true;
            String newDescription = "Modified Description";
            body.put("title",newTitle);
            body.put("completed",newCompleted);
            body.put("active",newActive);
            body.put("description",newDescription);
            long start = System.nanoTime();
            Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl+"/"+i);
            long end = System.nanoTime();
            long time = end-start;
            double time_in_second = (double) time / 1_000_000_000;
            double cpuUsage = osBean.getCpuLoad() * 100;
            long memory = osBean.getFreeMemorySize()/ (1024L * 1024L);
            if(i == target[index]-1){
                timeStore[index] = time_in_second;
                cpuUsageStore[index] = cpuUsage;
                freeMemoryStore[index] = memory;
                index++;
            }
        }
        System.out.println("---------Amend Project Statistics---------");
        System.out.printf("%-10s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)");
        for (int i = 0; i < target.length; i++) {
            System.out.printf("%-10d %-20.8f %-20.4f %-20d%n",
                    target[i],
                    timeStore[i],
                    cpuUsageStore[i],
                    freeMemoryStore[i]);
        }
        System.out.println("----------------------------------------");
    }


    @Test
    public void deleteProjects(){
        double[] timeStore = new double[target.length];
        double[] cpuUsageStore = new double[target.length];
        long[] freeMemoryStore = new long[target.length];

        for(int i=0;i<tot;i++){
            JSONObject body = new JSONObject();
            String newTitle = "New project " + i;
            Boolean newCompleted = false;
            Boolean newActive = false;
            String newDescription = "New Description";
            body.put("title",newTitle);
            body.put("completed",newCompleted);
            body.put("active",newActive);
            body.put("description",newDescription);
            Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
        }

        int index = 0;

        for(int i=0;i<tot;i++){
            long start = System.nanoTime();
            Response response = RestAssured.given().delete(pUrl+"/"+i);
            long end = System.nanoTime();
            long time = end-start;
            double time_in_second = (double) time / 1_000_000_000;
            double cpuUsage = osBean.getCpuLoad() * 100;
            long memory = osBean.getFreeMemorySize()/ (1024L * 1024L);
            if(i == target[index]-1){
                timeStore[index] = time_in_second;
                cpuUsageStore[index] = cpuUsage;
                freeMemoryStore[index] = memory;
                index++;
            }
        }
        System.out.println("---------Delete Project Statistics---------");
        System.out.printf("%-10s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)");
        for (int i = 0; i < target.length; i++) {
            System.out.printf("%-10d %-20.8f %-20.4f %-20d%n",
                    target[i],
                    timeStore[i],
                    cpuUsageStore[i],
                    freeMemoryStore[i]);
        }
        System.out.println("----------------------------------------");
    }
}
