package PerformanceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
public class ProjectsPerformanceTest {
    private static final String pUrl = "http://localhost:4567/projects";
    private static ProcessBuilder pb;

    private final int[] target = {1,10,50,100,200,500,1000};
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

    /*
    Performance test for creating project with 'Post' and 'JSON'
     */
    @Test
    public void createProjectsPost() {
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
            RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
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
            System.out.printf("%-10d %-20f %-20f %-20d%n",
                    target[i],
                    timeStore[i],
                    cpuUsageStore[i],
                    freeMemoryStore[i]);
        }
        System.out.println("----------------------------------------");
    }


    /*
    Performance test for amending project with 'Put' and 'JSON'
     */
    @Test
    public void amendProjectsPut(){
        double[] timeStore = new double[target.length];
        double[] cpuUsageStore = new double[target.length];
        long[] freeMemoryStore = new long[target.length];

        int index = 0;

        for(int i=0;i<tot;i++){
            JSONObject postBody = new JSONObject();
            String newTitle = "New project " + i;
            Boolean newCompleted = false;
            Boolean newActive = false;
            String newDescription = "New Description";
            postBody.put("title",newTitle);
            postBody.put("completed",newCompleted);
            postBody.put("active",newActive);
            postBody.put("description",newDescription);
            Response r1 = RestAssured.given().contentType(ContentType.JSON).body(postBody.toString()).post(pUrl);

            String id = r1.jsonPath().get("id");
            JSONObject amendBody = new JSONObject();
            String modifiedTitle = "modified project ";
            Boolean modifiedCompleted = true;
            Boolean modifiedActive = true;
            String modifiedDescription = "Modified Description";
            amendBody.put("title",modifiedTitle);
            amendBody.put("completed",modifiedCompleted);
            amendBody.put("active",modifiedActive);
            amendBody.put("description",modifiedDescription);


            long start = System.nanoTime();
            RestAssured.given().contentType(ContentType.JSON).body(amendBody.toString()).put(pUrl+"/"+id);
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
            System.out.printf("%-10d %-20f %-20f %-20d%n",
                    target[i],
                    timeStore[i],
                    cpuUsageStore[i],
                    freeMemoryStore[i]);
        }
        System.out.println("----------------------------------------");
    }


    /*
    Performance test for deleting project
     */
    @Test
    public void deleteProjects(){
        double[] timeStore = new double[target.length];
        double[] cpuUsageStore = new double[target.length];
        long[] freeMemoryStore = new long[target.length];

        int index = 0;

        for(int i=0;i<tot;i++) {
            JSONObject body = new JSONObject();
            String newTitle = "New project " + i;
            Boolean newCompleted = false;
            Boolean newActive = false;
            String newDescription = "New Description";
            body.put("title", newTitle);
            body.put("completed", newCompleted);
            body.put("active", newActive);
            body.put("description", newDescription);
            Response r1 = RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post(pUrl);
            String id = r1.jsonPath().get("id");
            if(i == target[index]-1){
                long start = System.nanoTime();
                RestAssured.given().delete(pUrl+"/"+ id);
                long end = System.nanoTime();
                long time = end-start;
                double time_in_second = (double) time / 1_000_000_000;
                double cpuUsage = osBean.getCpuLoad() * 100;
                long memory = osBean.getFreeMemorySize()/ (1024L * 1024L);
                timeStore[index] = time_in_second;
                cpuUsageStore[index] = cpuUsage;
                freeMemoryStore[index] = memory;
                index++;
            }
        }
        System.out.println("---------Delete Project Statistics---------");
        System.out.printf("%-10s %-20s %-20s %-20s%n", "SIZE", "TIME (s)", "CPU USAGE (%)", "MEMORY (MB)");
        for (int i = 0; i < target.length; i++) {
            System.out.printf("%-10d %-20f %-20f %-20d%n",
                    target[i],
                    timeStore[i],
                    cpuUsageStore[i],
                    freeMemoryStore[i]);
        }
        System.out.println("----------------------------------------");
    }
}
