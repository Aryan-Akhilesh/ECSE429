import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.junit.Assert;

public class APICategoriesTests{
    @Test
    public void getAllCategories(){
        Response res = RestAssured.get("http://localhost:4567/categories");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        System.out.println("Body:" + res.getBody().asString());

        Assert.assertEquals(200,statusCode);
    }

    @Test
    public void getCategoryInvalidId(){
        Response res = RestAssured.get("http://localhost:4567/todos/0");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        System.out.println("Body:" + res.getBody().asString());

        Assert.assertEquals(404,statusCode);

    }
    
}