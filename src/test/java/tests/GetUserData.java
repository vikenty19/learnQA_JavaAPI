package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class GetUserData extends BaseTest {
    @Test
    public void getResponseWithoutAuth() {
  /*  Map<String,String > authData = new HashMap<>();
    authData.put("email","vinkotov@example.com");
    authData.put("password","1234");*/
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        System.out.println(responseUserData.asString());
        Assertions.assertResponseHasField(responseUserData, "username");
        Assertions.assertResponseHasNotField(responseUserData, "email");
        Assertions.assertResponseHasNotField(responseUserData, "firstName");
        Assertions.assertResponseHasNotField(responseUserData, "lastName");
    }

    @Test
    public void getResponseWithAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        String header = getHeader(responseGetAuth, "x-csrf-token");
        String cookie = getCookie(responseGetAuth, "auth_sid");


        Response responseGetUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        String[] expectedFields = {"username", "email", "firstName", "lastName"};
        (Assertions.assertResponseHasFields(responseGetUserData, expectedFields);


    /*    Assertions.assertResponseHasField(responseGetUserData,"username");
        Assertions.assertResponseHasField(responseGetUserData,"email");
        Assertions.assertResponseHasField(responseGetUserData,"firstName");
        Assertions.assertResponseHasField(responseGetUserData,"lastName");*/
    }


}
