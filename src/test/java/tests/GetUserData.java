package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class GetUserData extends BaseTest {
@Test
    public void getResponseWithoutAuth(){
  /*  Map<String,String > authData = new HashMap<>();
    authData.put("email","vinkotov@example.com");
    authData.put("password","1234");*/
    Response responseUserData = RestAssured
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();
    System.out.println(responseUserData.asString());
   Assertions.assertResponseHasKey(responseUserData,"username");
   Assertions.assertResponseHasNotKey(responseUserData,"email");
    Assertions.assertResponseHasNotKey(responseUserData,"firstName");
    Assertions.assertResponseHasNotKey(responseUserData,"lastName");
}
    @Test
    public void getResponseWithAuthAsSameUser(){
    Map<String,String > authData = new HashMap<>();
    authData.put("email","vinkotov@example.com");
    authData.put("password","1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
     String header = getHeader(responseGetAuth,"x-csrf-token");
     String cookie = getCookie(responseGetAuth,"auth_sid");


     Response responseGetUserData = RestAssured
             .given()
             .header("x-csrf-token",header)
             .cookie("auth_sid",cookie)
             .get("https://playground.learnqa.ru/api/user/2")
             .andReturn();

        Assertions.assertResponseHasKey(responseGetUserData,"username");
        Assertions.assertResponseHasKey(responseGetUserData,"email");
        Assertions.assertResponseHasKey(responseGetUserData,"firstName");
        Assertions.assertResponseHasKey(responseGetUserData,"lastName");
    }


}
