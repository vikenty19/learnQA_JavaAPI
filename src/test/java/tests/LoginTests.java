package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTest;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import lib.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTests extends BaseTest {
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
        this.cookie = getCookie(responseAuth,"auth_sid");

        this.header = this.getHeader(responseAuth,"x-csrf-token");

        this.authId = this.getIntFromJson(responseAuth,"user_id");
    }



    @Test
    public void testAuthUser() {


        Response userConfirmId = RestAssured.given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();
       userConfirmId.prettyPrint();// System.out.println(userConfirmId.asString()); the same result


        Assertions.assertJsonByName(userConfirmId,"user_id",this.authId);
     /*   int authIdOnCheck = userConfirmId.getInt("user_id");
        assertTrue(authIdOnCheck > 0,"unexpected user id");*/
       assertEquals(userConfirmId.jsonPath().getInt("user_id"), this.authId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void negativeLoginTest(String conditions) {

           // two ways to define variable spec

    //    spec = new RequestSpecBuilder()
     //           .setBaseUri("https://playground.learnqa.ru/api/user/auth")
     //           .build();

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");
        // define string value

        if (conditions.equals("cookie")) {
            spec.cookie("auth_sid",this.cookie);
        } else if (conditions.equals("headers")) {
            spec.header("x-csrf-token", this.header);
        } else {
            throw new IllegalArgumentException("Value is  " + conditions);
        }

        JsonPath checkUserAuth = spec.get().jsonPath();

        assertEquals(0, checkUserAuth.getInt("user_id"), "user_id should be  0");
        System.out.println(checkUserAuth.prettyPrint());

        System.out.println("-----user with value  '" + conditions + "'   doesn't exist-----");
    }

}
