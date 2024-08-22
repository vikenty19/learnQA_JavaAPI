import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import io.restassured.http.Headers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JunitAndParam {
    @Test
    public void testFor200(){

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertEquals(200,response.statusCode(),"Unexpected status code");
    }
    @Test
    public void testFor404(){

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map1")
                .andReturn();
        assertEquals(404,response.statusCode(),"Unexpected status code");
    }
    @ParameterizedTest
    @ValueSource(strings = {"","Vasya","John","<body></body>"})
    public void helloWorldWithParams(String name){
        Map<String,String> queryParams = new HashMap<>();
        if (name.length() > 0){
            queryParams.put("name",name);

        }
        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedAnswer = (name.length()>0)? name:"someone";
        System.out.println(expectedAnswer);
        assertEquals("Hello, " + expectedAnswer,answer,"Unexpected answer");
    }
    @Test
    public void testAuthUser(){
        Map<String,String>authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String,String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();
        int user_id = responseGetAuth.jsonPath().getInt("user_id");
         //Assertions
        assertEquals(200,responseGetAuth.statusCode(),"unexpected status code");
        assertTrue(cookies.containsKey("auth_sid"),"Response doesn't return auth cookie");
        assertTrue(headers.hasHeaderWithName("x-csrf-token"),"Response doesn't return csrf - token");
        assertTrue((user_id>0),"User id must be greater then 0");
         // Check user registration with token
        Header token = headers.get("x-csrf-token");
        String authCookie = cookies.get("auth_sid");
        System.out.println(token + " \n  " + responseGetAuth.getHeader("x-csrf-token"));
        JsonPath responseCheckAuth = RestAssured
                .given()
               // .header("x-csrf-token",responseGetAuth.getHeader("x-csrf-token"))
                .header(token)
               // .cookie("auth_sid",responseGetAuth.getCookie("auth_sid"))
                .cookie("auth_sid",authCookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();

        int userIdForCheck = responseCheckAuth.getInt("user_id");
        assertEquals(user_id,userIdForCheck,"user id on reg doesn't match ");
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie","header"})
            public void loginWithoutCookieOrHeader(String condition){
    Map<String,String>authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
    Response responseGetAuth = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn();

    Map<String,String> cookies = responseGetAuth.getCookies();
    Headers headers = responseGetAuth.getHeaders();
        RequestSpecification spec = RestAssured
                .given()
                .baseUri("https://playground.learnqa.ru/api/user/auth");
        if (condition.equals("cookie")) {
             spec.cookie("auth_sid",cookies.get("auth_sid"));

        }else if(condition.equals("header")){

            spec.header("x-csrf-token",headers.get("x-csrf-token"));
        } else {
            throw new IllegalArgumentException("Condition value is  "+ condition);
        }
        JsonPath responseForCheck = spec.get().jsonPath();
        assertEquals(0,responseForCheck.getInt("user_id"),"User id must be 0 for unauth user");


}
}
