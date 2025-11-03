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

import static io.restassured.RestAssured.given;

public class UserEditTest extends BaseTestCase {
    protected final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @Test
    public void testEditJustCreatedUser(){
        //Generate user
        Map<String,String> createData = DateGenerator.getRegistrationData();
        JsonPath responseCreateAuth = given()
                .body(createData)
                .post(urlReg)
                .jsonPath();
        String userId = responseCreateAuth.getString("id");

        //Login user
        Map<String,String>loginData = new HashMap<>();
        loginData.put("email",createData.get("email"));
        loginData.put("password",createData.get("password"));
        Response responseLoginData = given()
                .body(loginData)
                .post(urlLogin)
                .andReturn();
        //Edit user name
        String newName = "New name";
        Map<String,String>editData = new HashMap<>();
        editData.put("firstName",newName);
   /*     Response responseEditUser= RestAssured
                .given()
                .body(editData)
                .header("x-csrf-token",this.getHeader(responseLoginData,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseLoginData,"auth_sid"))
                .put(urlReg + userId)
                .andReturn();*/
        //way to put query without object creation
                 given()
                .body(editData)
                .header("x-csrf-token",this.getHeader(responseLoginData,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseLoginData,"auth_sid"))
                .put(urlReg + userId);




        // Get new useData
        Response responseUserNewData = given()
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
    JsonPath responseCreateAuth = given()
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
    Response responseEditUser= given()
            .body(editData)
            .put("https://playground.learnqa.ru/api/user/" + userId)
            .andReturn();
    // Response responseEditUser = apiCoreRequest
      //       .makePostRequestUnauthorized("https://playground.learnqa.ru/api/user/" + userId,editData);

     // get new user data
    Response responseUserNewData = given()
            .get("https://playground.learnqa.ru/api/user/" + userId)
            .andReturn();
    System.out.println(responseUserNewData.asString());

 Assertions.assertJsonByName(responseUserNewData,"username","learnqa");


}

}
