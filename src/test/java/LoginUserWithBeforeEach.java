import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginUserWithBeforeEach {


    String cookie;
    String header;
    int userIdOnAuth;

    @BeforeEach
    public void loginUser() {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");
        Response response = given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        this.cookie = response.getCookie("auth_sid");
        this.header = response.getHeader("x-csrf-token");
        this.userIdOnAuth = response.jsonPath().getInt("user_id");

    }

    @Test
    public void authUserTest() {


        JsonPath responseCheckAuth = given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();
        int userIdForCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdForCheck > 0, "user id must be greater than 0" + userIdForCheck);
        assertEquals(userIdOnAuth, userIdForCheck, "User id not equals to expected" + userIdForCheck);

    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "header"})
    public void negativeAuthTest(String condition) {
        RequestSpecification requestSpecification = RestAssured
                .given().baseUri("https://playground.learnqa.ru/api/user/auth");
        if (condition.equals("cookie")) {
            requestSpecification.cookie("auth_sid", this.cookie);
        } else if (condition.equals("header")) {
            requestSpecification.header("x-csrf-token", this.header);

        } else {
            throw new IllegalArgumentException("Unknown condition value ----> " + condition);
        }
        JsonPath userIdForCheck = requestSpecification.get().jsonPath();
        userIdForCheck.prettyPrint();
        assertEquals(0, userIdForCheck.getInt("user_id"));
    }
}


