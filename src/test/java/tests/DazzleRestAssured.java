package tests;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class DazzleRestAssured {
    @Test
    public void restAssuredTest() {
        given()
                .when()
                .get("https://restful-booker.herokuapp.com/ping")
                .then()
                //   .assertThat()//works without this line
                .statusCode(201);
    }

    @Test
    public void login() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");
        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        response.then().log().body();//cookie,status,header,
        String id_user = response.jsonPath().getString("user_id");
        System.out.println("user ID  = " + id_user);


        given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK");
    }

    @Test
    public void createUserInDummyAPIAndUpdate() {
        Map<String, String> body1 = new HashMap<>();
        body1.put("name", "Max");
        body1.put("salary", "200000");
        body1.put("age", "33");
        File body = new File("./src/main/resources/credentials");
        JsonPath result = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("https://dummy.restapiexample.com/api/v1/create")
                .jsonPath();
        Assert.assertTrue(result.getString("status").equalsIgnoreCase("Success"));
        int user_id = result.getInt("data.id");//just in case
        result.prettyPrint();
    }

    @Test
    public void putRequestWithJsonObject() {
        JSONObject body1 = new JSONObject();
        body1.put("name", "Max");
        body1.put("salary", "200000");
        body1.put("age", "33");
        JsonPath response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body1.toString())// Highly important to use toString!!
                .put("https://dummy.restapiexample.com/api/v1/update/24")
                .jsonPath();
        Assert.assertEquals(response.getString("data.age"), body1.getString("age"));
        response.prettyPrint();

    }

    @Test
    public void logIfFailed() {
        given()
                .baseUri("https://restcountries.eu/rest/v2")
                .when()
                .get("/alpha/GB")
                .then()
                .log().ifError();

    }

    @Test
    public void logIfValidationFailed() {
        given()
                .baseUri("https://restcountries.eu/rest/v2")
                .when()
                .get("/alpha/GB")
                .then()
                .log().ifValidationFails()
                .statusCode(200);//validation MUST be after .log.validationFails()
    }
    @Test
    public void handlingFormData(){
        given()
                .baseUri("https://postman-echo.com")
                // content type may be differed, but need to be put
                .contentType("application/x-www-form-urlencoded;charset=UTF-8")
                .formParam("firstname","John")
                .formParam("lastname","Dou")
                .when()
                .post("/post")
                .then()
                .log().body()
                .statusCode(200);


    }
}