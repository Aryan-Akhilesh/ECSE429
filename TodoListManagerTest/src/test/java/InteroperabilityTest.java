import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.Random.class)
public class InteroperabilityTest {

    @BeforeAll
    public void startServer() {
        RestAssured.baseURI = "http://localhost:4567";
    }

    @AfterAll
    public void shutServer() {
        throw new NotImplementedException();
    }

    @Test
    public void failWhenServerDown() {
//        RestAssured.get("/shutdown");
        try {
            Response response = RestAssured.get("http://localhost:4567/todos");
            int status = response.getStatusCode();
            Assert.assertEquals(200, status);
        }
        catch (Exception e) {
            System.out.println("Server is down.");
        }
    }
}
