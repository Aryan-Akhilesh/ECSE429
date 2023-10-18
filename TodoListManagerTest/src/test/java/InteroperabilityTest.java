import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.Assert;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.Random.class)
public class InteroperabilityTest {

    private ProcessBuilder pb = new ProcessBuilder("java", "-jar", "TodoListManagerTest/src/test/resources/runTodoManagerRestAPI-1.5.5.jar");
    private Process p;

    @BeforeEach
    //@Test
    void startServer() throws IOException {
        p = pb.start();
        RestAssured.baseURI = "http://localhost:4567";
    }

    @AfterEach
    void shutServer() {
        RestAssured.get("http://localhost:4567/shutdown");
        p.destroy();
    }

    @Test
    public void failWhenServerDown() {
        try {
            RestAssured.get("/shutdown");
            Response response = RestAssured.get("http://localhost:4567/todos");
            int status = response.getStatusCode();
            Assert.assertNotEquals(200, status);
        }
        catch (Exception e) {
            System.out.println("Server is down.");
        }
    }


}
