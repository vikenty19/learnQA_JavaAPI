import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthCheck {

    @Test
    public void testAuthUser(){

        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        assertEquals(200,responseGetAuth.statusCode(),"unexpected status code");
     Map<String,String > cookies = responseGetAuth.getCookies();
        Headers headers =  responseGetAuth.getHeaders();
        int userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");
   //   assertEquals(200,responseGetAuth.statusCode(),"unexpected status code");
      assertTrue(cookies.containsKey("auth_sid"),"Response doesn't have 'auth cookie ");
      assertTrue(headers.hasHeaderWithName("x-csrf-token"),"Response doesn't have 'x-csrf-token' header");
        assertTrue((userIdOnAuth > 0),"Response user_id should be greater than 0");

        JsonPath responseCheckAuth = RestAssured
                .given()
                .cookie("auth_sid",responseGetAuth.getCookie("auth_sid"))
                .header("x-csrf-token",responseGetAuth.getHeader("x-csrf-token"))
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
      assertTrue(userIdOnCheck > 0,"user_id must be greater than 0 "+ userIdOnCheck);
      assertEquals(userIdOnAuth,userIdOnCheck,"user_id on auth not equal user_id on check");

    }
 @ParameterizedTest
    @ValueSource(strings = {"cookie","header"})
    public void authWithoutCookieOrHeader(String condition) {
     Map<String, String> authData = new HashMap<>();
     authData.put("email", "vinkotov@example.com");
     authData.put("password", "1234");
     Response responseGetAuth = RestAssured
             .given()
             .body(authData)
             .post("https://playground.learnqa.ru/api/user/login")
             .andReturn();
     Map<String, String> cookies = responseGetAuth.getCookies();
     Headers headers = responseGetAuth.getHeaders();

     // request with different argument
     RequestSpecification spec = RestAssured.given();
     spec.baseUri("https://playground.learnqa.ru/api/user/auth");
     if (condition.equals("cookie")) {
         spec.cookie("auth_sid", cookies.get("auth_sid"));
     } else if (condition.equals("header")) {
         spec.header("x-csrf-token", headers.get("x-csrf-token"));
     } else {
         throw new IllegalArgumentException("condition value is  " + condition);
     }
      //request
     JsonPath authForCheck = spec.get().jsonPath();
     System.out.println(authForCheck.getInt("user_id"));
     assertEquals(0,authForCheck.getInt("user_id"),"User id should be 0 for unauthorized request");
 }

}
