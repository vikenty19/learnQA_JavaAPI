import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeforeEachClass {

    public class UserLoginCheck {
        String cookie;
        String header;
        int userIdOnAuth;

        @BeforeEach
        public void successLogin() {
            Map<String, String> authData = new HashMap<>();
            authData.put("email", "vinkotov@example.com");
            authData.put("password", "1234");
            Response responseGetAuth = RestAssured
                    .given()
                    .body(authData)
                    .post("https://playground.learnqa.ru/api/user/login")
                    .andReturn();
            this.cookie = responseGetAuth.getCookie("auth_sid");
            this.header = responseGetAuth.getHeader("x-csrf-token");
            this.userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");

        }

        @Test
        public void testAuthUser() {


            JsonPath responseCheckAuth = RestAssured
                    .given()
                    .cookie("auth_sid", this.cookie)
                    .header("x-csrf-token", this.header)
                    .get("https://playground.learnqa.ru/api/user/auth")
                    .jsonPath();

            int userIdOnCheck = responseCheckAuth.getInt("user_id");
            assertTrue(userIdOnCheck > 0, "user_id must be greater than 0 " + userIdOnCheck);
            assertEquals(userIdOnAuth, userIdOnCheck, "user_id on auth not equal user_id on check");

        }

        @ParameterizedTest
        @ValueSource(strings = {"cookie", "header"})
        public void authWithoutCookieOrHeader(String condition) {

            // request with different argument
            RequestSpecification spec = RestAssured.given();
            spec.baseUri("https://playground.learnqa.ru/api/user/auth");
            if (condition.equals("cookie")) {
                spec.cookie("auth_sid", this.cookie);
            } else if (condition.equals("header")) {
                spec.header("x-csrf-token", this.header);
            } else {
                throw new IllegalArgumentException("condition value is  " + condition);
            }
            //request
            JsonPath authForCheck = spec.get().jsonPath();
            System.out.println(authForCheck.getInt("user_id"));
            assertEquals(0, authForCheck.getInt("user_id"), "User id should be 0 for unauthorized request");
        }

    }

}
