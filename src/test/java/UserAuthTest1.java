import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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

import static org.junit.jupiter.api.Assertions.*;

public class UserAuthTest1 {
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
        this.cookie = response.getCookie("auth_sid");
        this.header = response.getHeader("x-csrf-token");
        this.userIdOnAuth = response.jsonPath().getInt("user_id");

    }

    @Test
    public void registerUserAndCheck() throws IOException {
               JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token",this.header)
                .cookie("auth_sid", this.cookie)
                .get(" https://playground.learnqa.ru/api/user/auth")
                .jsonPath();
        int userIdForCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdForCheck > 0, "user id must be greater than 0  " + userIdForCheck);
        assertEquals(userIdOnAuth, userIdForCheck, "User id not equals to expected" + userIdForCheck);

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


        RequestSpecification requestSpecification = RestAssured
                .given().baseUri(properties.getProperty("authUrl"));

        if (condition.equals("cookie")) {
            requestSpecification.cookie("auth_sid", this.cookie);
        } else if (condition.equals("header")) {
            requestSpecification.header("x-csrf-token",this.header);

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
