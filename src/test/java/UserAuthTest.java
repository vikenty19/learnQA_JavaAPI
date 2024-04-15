import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

public class UserAuthTest {
    @Test
    public void registerUserAndCheck(){
        Map<String,String> userData = new HashMap<>();
        userData.put("email","vinkotov@example.com");
        userData.put("password","1234");
        Response response = given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        Map<String,String>coockies = response.getCookies();
        assertTrue(coockies.containsKey("auth_sid"));// можно так или через string (показано ниже)

        String authCookie = response.getCookie("auth_sid");
     int userId = response.jsonPath().getInt("user_id");
       Headers headers = response.getHeaders();
       assertEquals(200,response.statusCode(),"Unexpected status code");
       assertNotEquals(null,authCookie,"AuthCookie doesn't exist");
    //    System.out.println(authCookie);
        assertTrue(headers.hasHeaderWithName("x-csrf-token"),"Response doesn't have header to auth");
        assertTrue((userId>0),"User id must be greater then zero");

//        Map<String,Object>checkAuth = new HashMap<>();
//        checkAuth.put("user_id",userId);
//        checkAuth.put("x-csrf-token",response.getHeader("x-csrf-token"));
         JsonPath responseCheckAuth = given()
                 .header("x-csrf-token",response.getHeader("x-csrf-token"))
                 .cookie("auth_sid",response.getCookie("auth_sid"))
                 .get("https://playground.learnqa.ru/api/user/auth")
                 .jsonPath();
         int userIdForCheck = responseCheckAuth.getInt("user_id");
         assertTrue(userIdForCheck > 0,"user id must be greater than 0"+ userIdForCheck);
         assertEquals(userId,userIdForCheck,"User id not equals to expected"+ userIdForCheck);

    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie","header"})
    public void negativeAuthTests(String condition){
        Map<String,String >userData = new HashMap<>();
        userData.put("email","vinkotov@example.com");
        userData.put("password","1234");
        Response response = given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        Map<String,String>cookies = response.getCookies();
       Headers headers = response.getHeaders();
       // spec for the first variant
      /*  RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://playground.learnqa.ru/api/user/auth")
                .build();*/
        //spe for the second variant
        RequestSpecification requestSpecification = RestAssured
                .given().baseUri("https://playground.learnqa.ru/api/user/auth");

        if(condition.equals("cookie")){
            requestSpecification.cookie("auth_sid",cookies.get("auth_sid"));
        }
        else if(condition.equals("header")){
            requestSpecification.header("x-csrf-token",headers.get("x-csrf-token"));

        }
        else {
            throw new IllegalArgumentException("Unknown condition value ----> " + condition);
        }

        //First variant
      /*     given().spec(requestSpecification).log().uri()
                   .when().get()
                   .then().body("user_id",equalTo(0)).log().body();*/


        // second variant

        JsonPath responseFoCheck = requestSpecification.get().jsonPath();
        responseFoCheck.prettyPrint();
        assertEquals(0,responseFoCheck.getInt("user_id"));
    }
}
