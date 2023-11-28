package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTest;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTest {

    @Test
    public void editJustCreatedUser() {

        // Create new user

        Map<String, String> userData = new HashMap<>();
        userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
   int userId = responseCreateAuth.getInt("id");
   //     System.out.println(responseCreateAuth.getInt("id"));

        //Login user

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseLoginUser = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
    //    System.out.println(responseLoginUser.asString());
        // Edit User

        String newUserName = "NEW NAME";
        Map<String, String> newData = new HashMap<>();
        newData.put("firstName", newUserName);
        Response editUser = RestAssured
                .given()
                .header("x-csrf-token",this.getHeader(responseLoginUser,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseLoginUser,"auth_sid"))
                .body(newData)
                .put("https://playground.learnqa.ru/api/user/"+ userId)
                .andReturn();



        // Get new userData

        Response responseGetUserNewData = RestAssured
                .given()
                .header("x-csrf-token", responseLoginUser.getHeader("x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseLoginUser, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
       System.out.println(responseGetUserNewData.asString());

        Assertions.assertJsonByName(responseGetUserNewData, "firstName", newUserName);
    }

    @Test
    public void changeUserDataWithoutAuth() {
        Map<String, String> userData = new HashMap<>();
        userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = responseCreateAuth.getString("id");
        String userLastName = responseCreateAuth.getString("lastName");
        System.out.println(userData);
        System.out.println(userId);

        String newLastName = "NewNameAgain";
        Map<String, String> changeUserData = new HashMap<>();
        changeUserData.put("lastName", newLastName);
        Response responseEditUser = RestAssured
                .given()
                .body(changeUserData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
        System.out.println(responseEditUser.asString());
        Response responseChangedUserData = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
        System.out.println(responseChangedUserData.asString());
        Assertions.assertResponseHasNotField(responseChangedUserData, userLastName);
    }

    @Test
    public void changeOtherUserData() {

        Map<String, String> userData = new HashMap<>();
        String newEmail = "vinkotov@example.com";
        userData = DataGenerator.getRegistrationData();
   //     System.out.println(userData);
  //      userData.put("email",newEmail);
  //      System.out.println(userData);
        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = responseCreateAuth.getString("id");
  /*      JsonPath responseCreateLoginData = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        responseCreateLoginData.prettyPrint();
       String userId = responseCreateLoginData.getString("id");*/

        System.out.println(userId);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));




        Response responseLoginUser = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
          responseLoginUser.prettyPrint();



       String newUserEmail = "NEW NAME";
        Map<String, String> newData = new HashMap<>();
        newData.put("email", newUserEmail);
        Response responseEditUserData = RestAssured
                .given()
                .header("x-csrf-token", responseLoginUser.getHeader("x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseLoginUser, "auth_sid"))
                .body(newData)
                .put("https://playground.learnqa.ru/api/user/" + (userId))
                .andReturn();

  //      System.out.println(responseEditUserData.asString());
        Response responseGetUserNewData = RestAssured
                .given()
                .header("x-csrf-token", responseLoginUser.getHeader("x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseLoginUser, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + (userId))
                .andReturn();
        System.out.println(responseGetUserNewData.asString());



    }
}