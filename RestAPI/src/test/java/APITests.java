import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.junit.Assert;


public class APITests{

    @Test
    public void getTodos(){
        Response res = RestAssured.get("http://localhost:4567/todos");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        System.out.println("Body:" + res.getBody().asString());

        Assert.assertEquals(200,statusCode);

    }

    @Test
    public void getTodoByID(){
        Response res = RestAssured.get("http://localhost:4567/todos/1");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        System.out.println("Body:" + res.getBody().asString());

        Assert.assertEquals(200,statusCode);
    }

    @Test
    public void getTodoByInvalidID(){
        Response res = RestAssured.get("http://localhost:4567/todos/999");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        System.out.println("Body:" + res.getBody().asString());

        Assert.assertEquals(404,statusCode);
    }

    @Test
    public void getCategoryOfTodo(){
        Response res = RestAssured.get("http://localhost:4567/todos/1/categories");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        System.out.println("Body:" + res.getBody().asString());

        //Assert.assertEquals("{\"categories\":[{\"id\":\"1\",\"title\":\"Office\",\"description\":\"\"}]}" , res.getBody().asString());
        Assert.assertEquals(200,statusCode);

    }

    @Test
    public void getTaskofTodo(){
        Response res = RestAssured.get("http://localhost:4567/todos/1/tasksof");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        System.out.println("Body:" + res.getBody().asString());

        Assert.assertEquals(200,statusCode);
    }

    @Test
    public void postTodoWithoutID(){
        String jsonString = "{" + "\"title\": \"get more paperwork\"," + "\"doneStatus\": false," +
                "\"description\": \"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," +
                "\"categories\": [" + "{" + "\"id\": \"1\"" + "}" + "]" + "}";

        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri("http://localhost:4567/todos");
        request.body(jsonString);
        Response response = request.post();
        System.out.println(response.asString());
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(201);
    }

    @Test
    public void postTodoWithID(){
        String jsonString = "{" + "\"id\": \"4\"," + "\"title\": \"Photocopy Documents\"," + "\"doneStatus\": false," +
                "\"description\": \"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," +
                "\"categories\": [" + "{" + "\"id\": \"1\"" + "}" + "]" + "}";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri("http://localhost:4567/todos");
        request.body(jsonString);
        Response response = request.post();
        System.out.println(response.asString());
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(400);
    }

    @Test
    public void postTodoWithDiffTitle(){
        String jsonString =  "{" + "\"id\": 1," +"\"title\": \"get more paperwork\"," + "\"doneStatus\": false," +
                "\"description\": \"\"," + "\"tasksof\": [" + "{" + "\"id\": \"1\"" + "}" + "]," +
                "\"categories\": [" + "{" + "\"id\": \"1\"" + "}" + "]" + "}";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri("http://localhost:4567/todos/1");
        request.body(jsonString);
        Response response = request.post();
        System.out.println(response.asString());
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(200);
    }

    @Test
    public void postWithoutBody(){
        String jsonString = "{}";
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri("http://localhost:4567/todos");
        request.body(jsonString);
        Response response = request.post();
        System.out.println(response.asString());
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(400);
    }

    @Test
    public void deleteExistingTodo(){

        Response res = RestAssured.delete(" http://localhost:4567/todos/2");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        Assert.assertEquals(200,statusCode);

        //restoring the 2nd entry
    }

    @Test
    public void deleteNonExistingTodo(){
        Response res = RestAssured.delete("http://localhost:4567/todos/999");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        System.out.println("Body:" + res.getBody().asString());
        Assert.assertEquals(404,statusCode);
    }

    @Test
    public void deleteCategoryFromTodo(){
        Response res = RestAssured.delete("http://localhost:4567/todos/1/categories/1");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        Assert.assertEquals(200,statusCode);
    }

    @Test
    public void deleteTaskOfTodo(){
        Response res = RestAssured.delete("http://localhost:4567/todos/1/tasksof/1");
        int statusCode = res.getStatusCode();

        System.out.println("Status Code:" + statusCode);
        System.out.println("Time Taken:" + res.getTime());
        Assert.assertEquals(200,statusCode);
    }
}
