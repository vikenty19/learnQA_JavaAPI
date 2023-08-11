import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTests {
    String cookie;
    String header;
    int authId;
    protected RequestSpecification spec;
    @BeforeEach
    public void logIn() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseAuth = RestAssured
                .given().body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        this.cookie = responseAuth.getCookie("auth_sid");
        this.header = responseAuth.getHeader("x-csrf-token");
        this.authId = responseAuth.jsonPath().getInt("user_id");
    }



    @Test
    public void testAuthUser() {


        JsonPath userConfirmId = RestAssured.given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();
        userConfirmId.prettyPrint();
        Assert.assertEquals(userConfirmId.getInt("user_id"), authId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void negativeLoginTest(String value) {

           // two ways to define variable spec

        spec = new RequestSpecBuilder()
                .setBaseUri("https://playground.learnqa.ru/api/user/auth")
                .build();

        RequestSpecification spec1 = RestAssured.given();
        spec1.baseUri("https://playground.learnqa.ru/api/user/auth");
        // define string value
        if (value.equals("cookie")) {
            spec.cookie("auth_sid", this.cookie);
        } else if (value.equals("headers")) {
            spec.header("x-csrf-token", this.header);
        } else {
            throw new IllegalArgumentException("Value is  " + value);
        }

        JsonPath checkUserAuth = spec1.get().jsonPath();
        assertEquals(0, checkUserAuth.getInt("user_id"), "user_id should be  0");
    }

}
