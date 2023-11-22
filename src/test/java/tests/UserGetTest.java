package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTest {
@Test
public void getUserDataNotAuth(){
    Response responseGetUserData = RestAssured
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn();
    System.out.println(responseGetUserData.asString());
    Assertions.assertJsonHasKey1(responseGetUserData,"username");
    Assertions.assertJsonHasNotKey1(responseGetUserData,"email");
    Assertions.assertJsonHasNotKey1(responseGetUserData,"firstname");
    Assertions.assertJsonHasNotKey1(responseGetUserData,"lastname");
}
    @Test
    public void getUserAuthDataAuth(){
     Map<String,String> userData = new HashMap<>();
     userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");
        Response responseGetAuthData = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        String header = getHeader(responseGetAuthData,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuthData,"auth_sid");
        Response responseWithAuthData = RestAssured
                .given()
                .header("x-csrf-token",header)
                .cookie("auth_sid",cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        String[] checkingData = {"username","firstName","lastName","email"};
        Assertions.assertResponseHasKeys(responseWithAuthData,checkingData);


    }




}
