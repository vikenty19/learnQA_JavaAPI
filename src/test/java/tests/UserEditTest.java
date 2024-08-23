package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequest;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DateGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    protected final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @Test
    public void testEditJustCreatedUser(){
        //Generate user
        Map<String,String> createData = DateGenerator.getRegistrationData();
        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(createData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = responseCreateAuth.getString("id");

        //Login user
        Map<String,String>loginData = new HashMap<>();
        loginData.put("email",createData.get("email"));
        loginData.put("password",createData.get("password"));
        Response responseLoginData = RestAssured
                .given()
                .body(loginData)
                .post(urlLogin)
                .andReturn();
        //Edit user name
        String newName = "New name";
        Map<String,String>editData = new HashMap<>();
        editData.put("firstName",newName);
        Response responseEditUser= RestAssured
                .given()
                .body(editData)
                .header("x-csrf-token",this.getHeader(responseLoginData,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseLoginData,"auth_sid"))
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        // Get new useData
        Response responseUserNewData = RestAssured
                .given()
                .header("x-csrf-token",this.getHeader(responseLoginData,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseLoginData,"auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
        System.out.println(responseUserNewData.asString());
        Assertions.assertJsonByName(responseUserNewData,"firstName",newName);

    }
@Test
 public void testEditUserByOtherUser(){
        //register user
    Map<String,String> authData = DateGenerator.getRegistrationData();
    JsonPath responseCreateAuth = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/")
            .jsonPath();
    String userId = responseCreateAuth.getString("id");
     System.out.println(userId+"  "+ authData.get("username"));

     //Edit other user without authorization
     String newUserName = "dark day";
     Map<String,String>editData = new HashMap<>();
     editData.put("username",newUserName);
    System.out.println(editData);
    Response responseEditUser= RestAssured
            .given()
            .body(editData)
            .put("https://playground.learnqa.ru/api/user/" + userId)
            .andReturn();
    // Response responseEditUser = apiCoreRequest
      //       .makePostRequestUnauthorized("https://playground.learnqa.ru/api/user/" + userId,editData);

     // get new user data
    Response responseUserNewData = RestAssured
            .given()
            .get("https://playground.learnqa.ru/api/user/" + userId)
            .andReturn();
    System.out.println(responseUserNewData.asString());




}

}
