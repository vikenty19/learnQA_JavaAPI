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
    String[]keys = {"email","username","firstName","lastName"};
    Assertions.assertJsonHasFields(responseUserData,keys);

}
@Test
public void getUserDataByAnotherRegisteredUser(){

    Response responseLoginUser = loginRegisteredUser();
    int UserId = responseLoginUser.jsonPath().getInt("user_id");
    String headerAuth = this.getHeader(responseLoginUser,"x-csrf-token");
    String cookieAuth = this.getCookie(responseLoginUser, "auth_sid");

    Response responseForCheckRegisteredUser= RestAssured.given()
            .cookie("cookie",cookieAuth)
            .header("header",headerAuth)
            .get(urlReg+UserId)
            .andReturn();
    Assertions.assertResponseCodeEquals(responseForCheckRegisteredUser,200);
    Response responseForCheckOtherUser= RestAssured.given()
            .cookie("cookie",cookieAuth)
            .header("header",headerAuth)
            .get(urlReg+Math.addExact(UserId,1))
            .andReturn();
 
    System.out.println(urlReg+Math.addExact(UserId,1));
    System.out.println(responseForCheckOtherUser.asString());

}

}

