import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class LoginTests {

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
                .header("x-csrf-token",responseAuth.getHeader("x-csrf-token"))
                .cookie("auth_sid",responseAuth.getCookie("auth_sid"))
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();
        userConfirmId.prettyPrint();
       Assert.assertEquals(userConfirmId.getInt("user_id"),authId);
    }


}
