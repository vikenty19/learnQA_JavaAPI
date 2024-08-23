package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequest;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class GetUserInfoTest extends BaseTestCase {
    ApiCoreRequest apiCoreRequest= new ApiCoreRequest();
    @Test
    public void getUserInfoNotAuth(){

        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertions.assertJsonHasValue(responseUserData,"username");
        Assertions.assertJsonHasNotValues(responseUserData,fields);
        System.out.println(responseUserData.asString());

    }
    @Test
    public void getUserDataAuthAsSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response getAuthResponse = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        String authCookie = this.getCookie(getAuthResponse,"auth_sid");
        String header = this.getHeader(getAuthResponse,"x-csrf-token");

        Response responseUserData = RestAssured
                .given()
                .cookie("auth_sid",authCookie)
                .header("x-csrf-token",header)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

            Assertions.assertJsonHasValues(responseUserData,fieldsForAuth);
        System.out.println(responseUserData.asString());
    }

    @Test
    public void getUserDataAuthAsOtherUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response getAuthResponse = apiCoreRequest
                .makePostRequest(urlLogin,authData);


       /* Response getAuthResponse = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();*/
        String authCookie = this.getCookie(getAuthResponse,"auth_sid");
        String header = this.getHeader(getAuthResponse,"x-csrf-token");

        Response responseUserData = apiCoreRequest
                .makeGetRequest("https://playground.learnqa.ru/api/user/1"
                ,header
                ,authCookie);

     /*   Response responseUserData = RestAssured
                .given()
                .cookie("auth_sid",authCookie)
                .header("x-csrf-token",header)
                .get("https://playground.learnqa.ru/api/user/1")
                .andReturn();*/

        Assertions.assertJsonHasNotValues(responseUserData,fieldsNotAuth);
        Assertions.assertResponseCodeEquals(responseUserData,200);
        Assertions.assertJsonHasValue(responseUserData,"username");
        System.out.println(responseUserData.asString());
    }
}
