package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.Matchers.hasKey;

public class UserGetTest extends BaseTestCase {
    @Test
    public void testGetUser(){//check user data without authorization
        Response response = RestAssured.given()
                .get(urlReg+"2")//userId =2
        .andReturn();
        Assertions.assertJsonHasValue(response,"username");
        Assertions.assertJsonHasNotValue(response,"firstname");
        Assertions.assertJsonHasNotValue(response,"lastname");
        Assertions.assertJsonHasNotValue(response,"email");
        System.out.println(response.asString());
    }

@Test
    public void getUserDataAuthSameUser() throws IOException {
    Properties properties = getProperty();
    Map<String, String> authData = new HashMap<>();
    authData.put("email", properties.getProperty("email"));
    authData.put("password", properties.getProperty("password"));
    Response responseGetAuth = RestAssured.given()
            .body(authData)
            .post(urlLogin)
            .andReturn();
    int userId = responseGetAuth.jsonPath().getInt("user_id");
    String header = this.getHeader(responseGetAuth, "x-csrf-token");
    String cookie = this.getCookie(responseGetAuth, "auth_sid");

    Response responseUserData = RestAssured.given()
            .header("x-csrf-token", header)
            .cookie("auth_sid", cookie)
            .get(urlReg + userId)
            .andReturn();
    Assertions.assertResponseCodeEquals(responseUserData, 200);
  /*  Assertions.assertJsonHasValue(responseUserData, "username");
    Assertions.assertJsonHasValue(responseUserData, "firstName");
    Assertions.assertJsonHasValue(responseUserData, "lastName");
    Assertions.assertJsonHasValue(responseUserData, "email");*/
    String[]keys = {"email","username","firstName","lastName"};
    Assertions.assertJsonHasFields(responseUserData,keys);

}

}

