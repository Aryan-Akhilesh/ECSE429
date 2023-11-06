package StoryTest;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.restassured.RestAssured;

import java.io.IOException;

public class Setup {

    private static ProcessBuilder pb;

    @BeforeAll
    public static void setupProcess() {
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

    @Before
    public void InitializeServerBefore() {
        try {
            pb.start();
            Thread.sleep(1000);
        } catch (IOException | InterruptedException e) {
            System.out.println("No server");
        }
    }

    @After
    public void shutServerAfter() {
        try {
            RestAssured.get("http://localhost:4567/shutdown");
        }
        catch (Exception ignored) {
        }
    }

}
