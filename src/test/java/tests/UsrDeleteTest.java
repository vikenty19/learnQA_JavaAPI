package tests;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lib.Assertions;
import lib.AuthService;
import lib.BaseTestCase;
import lib.DateGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsrDeleteTest extends BaseTestCase {
 protected Properties properties;

    @Test
            public void deleteExistingUser() throws IOException {
        properties=AuthService.readProperties();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", properties.getProperty("email"));
        userData.put("password",properties.getProperty("password"));
        System.out.println(userData);
        Response responseGetAuth = RestAssured.given()
                .body(userData)
                .post(urlLogin)
                .andReturn();
        int userId = responseGetAuth.jsonPath().getInt("user_id");
       String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        System.out.println(responseGetAuth.asString());
        Response responseForDelete=RestAssured
                .given()
                .header("x-csrf-token",header)
                .cookie("auth_sid",cookie)
                .pathParams("user",userId)
                .when()
                .delete(urlLogin+"/{user}")  //one of the way to pass path parameters
                .andReturn();
        System.out.println(responseForDelete.getStatusCode());
        Assertions.assertResponseCodeEquals(responseForDelete,404);

    }
    @Test
    public void deleteJustCreatedUser(){
        //register new user
        Map<String, String> data = DateGenerator.getRegistrationData();
        Response registerUser =RestAssured
                .given()
                .body(data)
                .post(urlReg)
                .andReturn();
        String userId = registerUser.jsonPath().getString("id");
        System.out.println(userId);
     //login this user
        Map<String,String>loginData = new HashMap<>();
        loginData.put("email",data.get("email"));
        loginData.put("password",data.get("password"));

        Response loginUser = RestAssured
                .given()
                .body(loginData)
                .post(urlLogin)
                .andReturn();
        loginUser.jsonPath().prettyPrint();
            //delete user
          Response deleteUser =RestAssured
                  .given()
                  .header("x-csrf-token",getHeader(loginUser,"x-csrf-token"))
                  .cookie("auth_sid",this.getCookie(loginUser,"auth_sid"))
                  .pathParams("user",userId)
                  .delete(urlReg+"{user}")
                  .andReturn();
        System.out.println(deleteUser.asString());
        Assertions.assertJsonHasField(deleteUser,"success");
        assertEquals(200,deleteUser.getStatusCode());
        // check whether user is deleted
        Response getDeletedUserInfo = RestAssured
                .get(urlReg + userId)
                .andReturn();
        Assertions.assertResponseTextEquals(getDeletedUserInfo,"User not found");
        Assertions.assertResponseCodeEquals(getDeletedUserInfo,404);

    }
}
