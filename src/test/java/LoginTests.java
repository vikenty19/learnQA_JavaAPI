import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTests {
    protected RequestSpecification spec;

    @Test
    public void testAuthUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseAuth = RestAssured
                .given().body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String, String> cookie = responseAuth.getCookies();
        Headers headers = responseAuth.getHeaders();
        int authId = responseAuth.jsonPath().getInt("user_id");
        System.out.println(cookie + "\n" + headers + "\n" + authId + "   statuscode is  " + responseAuth.getStatusCode());
        Assert.assertEquals(200, responseAuth.getStatusCode());
        Assert.assertTrue(cookie.containsKey("auth_sid"));
        Assert.assertTrue(authId > 0);


        JsonPath userConfirmId = RestAssured.given()
                .header("x-csrf-token", responseAuth.getHeader("x-csrf-token"))
                .cookie("auth_sid", responseAuth.getCookie("auth_sid"))
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();
        userConfirmId.prettyPrint();
        Assert.assertEquals(userConfirmId.getInt("user_id"), authId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void negativeLoginTest(String value) {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseAuth = RestAssured
                .given().body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        Map<String, String> cookies = responseAuth.getCookies();
        Headers headers = responseAuth.getHeaders();
        // two ways to define spec
        spec = new RequestSpecBuilder()
                .setBaseUri("https://playground.learnqa.ru/api/user/auth")
                .build();

        RequestSpecification spec1 = RestAssured.given();
        spec1.baseUri("https://playground.learnqa.ru/api/user/auth");
         // define string value
        if(value.equals("cookie")){
            spec.cookie("auth_sid",cookies.get("auth_sid"));
        } else if (value.equals("headers")) {
            spec.header("x-csrf-token",headers.get("x-csrf-token"));
        }else {
            throw new IllegalArgumentException("Value is  "+ value);
        }

        JsonPath checkUserAuth = spec1.get().jsonPath();
        assertEquals(0,checkUserAuth.getInt("user_id"),"user_id should be  0");
    }

}
