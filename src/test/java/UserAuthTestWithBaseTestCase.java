import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class UserAuthTestWithBaseTestCase extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;
    Properties properties;

       @BeforeEach
    public void loginUser() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");
        Response response = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        //Instead of  the  assertions in test using getCookie from BaseTestCase
        this.cookie = this.getCookie(response,"auth_sid");
        this.header = this.getHeader(response,"x-csrf-token");
        this.userIdOnAuth = this.getIntFromResponse(response,"user_id");

    }

    @Test
    public void registerUserAndCheck() throws IOException {


        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token",this.header)
                .cookie("auth_sid",this.cookie)
                .get(urlAuth)
                .jsonPath();
        int userIdForCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdForCheck > 0, "user id must be greater than 0  " + userIdForCheck);
        assertEquals(userIdOnAuth, userIdForCheck, "User id not equals to expected" + userIdForCheck);

    }

    @Test
    public void authUser() throws IOException {
        properties= new Properties();
       File data = new File("./src/test/java/lib/properties");
        FileInputStream loadData = new FileInputStream(data);
        properties.load(loadData);
        Map<String, String> authData = new HashMap<>();
        authData.put("email", properties.getProperty("email"));
        authData.put("password", properties.getProperty("password"));
        Response responseForAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        String authCookie = responseForAuth.getCookie("auth_sid");
        int userId = responseForAuth.jsonPath().getInt("user_id");
        String token = responseForAuth.getHeader("x-csrf-token");
        //   System.out.println( userId);
        JsonPath responseForReg = RestAssured
                .given()
                .header("x-csrf-token", token)
                .cookie("auth_sid", authCookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();
        System.out.println(userId + " userId  --->" + responseForReg.getInt("user_id"));
        assertEquals(userId, responseForReg.getInt("user_id"), "user has not the same id");


    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "header"})
    public void negativeAuthTests(String condition) throws IOException {
        Response response= loginSuccess();
        Map<String, String> cookies = response.getCookies();
        Headers headers = response.getHeaders();
        // spec for the first variant
      /*  RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://playground.learnqa.ru/api/user/auth")
                .build();*/

        //spec for the second variant
       // RequestSpecification requestSpecification = RestAssured
        //        .given().baseUri("https://playground.learnqa.ru/api/user/auth");
        RequestSpecification requestSpecification = RestAssured
                .given().baseUri(properties.getProperty("authUrl"));

        if (condition.equals("cookie")) {
            requestSpecification.cookie("auth_sid", cookies.get("auth_sid"));
        } else if (condition.equals("header")) {
            requestSpecification.header("x-csrf-token", headers.get("x-csrf-token"));

        } else {
            throw new IllegalArgumentException("Unknown condition value ----> " + condition);
        }

        //First variant
      /*     given().spec(requestSpecification).log().uri()
                   .when().get()
                   .then().body("user_id",equalTo(0)).log().body();*/


        // second variant

        JsonPath userIdForCheck = requestSpecification.get().jsonPath();
        userIdForCheck.prettyPrint();
        assertEquals(0, userIdForCheck.getInt("user_id"));
    }

    public Response loginSuccess() throws IOException {
        properties = new Properties();
        File data = new File("./src/test/java/lib/properties");
        FileInputStream loadData = new FileInputStream(data);
        properties.load(loadData);
        Map<String, String> authData = new HashMap<>();
        authData.put("email", properties.getProperty("email"));
        authData.put("password", properties.getProperty("password"));
        return RestAssured
                .given()
                .body(authData)
                .post(properties.getProperty("loginUrl"))
                .andReturn();
    }
}
