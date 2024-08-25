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

public class DeleteUserTest extends BaseTestCase {
    protected final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @Test
    public void testDeleteProtectedUser(){
        Map<String,String>data = new HashMap<>();
        data.put("email","vinkotov@example.com");
        data.put("password","1234");
        String URL = urlReg + "2";
        System.out.println(URL);
               Response responseToDelete = apiCoreRequest
                .deleteProtectedUser(URL,data);
      /*  Response responseToDelete = RestAssured
                .given()
                .body(data)
                .delete(urlReg + "/1")
                .andReturn();*/
          responseToDelete.jsonPath().prettyPrint();

        Assertions.assertResponseCodeEquals(responseToDelete,400);
        Assertions.assertJsonHasValue(responseToDelete,"error");


    }
   @Test
    public void testDeleteJustCreatedUser(){
        //Create user
       UserAuthTest userAuthTest = new UserAuthTest();
        Map<String,String>data = DateGenerator.getRegistrationData();
       JsonPath responseUserReg = RestAssured
               .given()
               .body(data)
               .post(urlReg)
               .jsonPath();
      String userID = responseUserReg.getString("id");

      //Login user
       userAuthTest.loginUser(data.get("email"));//=>reload loginUser method

         System.out.println(userAuthTest.user_id);

      //Delete created user
      Response responseDeletedUser =RestAssured
               .given()
               .header("x-csrf-token",userAuthTest.header)
               .cookie("auth_sid",userAuthTest.cookie)
               .delete(urlReg+userID)
               .andReturn();

       //Check If User deleted

       Response getDeletedUserInfo = RestAssured
                .get(urlReg + userID)
               .andReturn();
    //   System.out.println(urlReg + userID);
    //   System.out.println(getDeletedUserInfo.asString());
      Assertions.assertResponseTextEquals(getDeletedUserInfo,"User not found");
      Assertions.assertResponseCodeEquals(getDeletedUserInfo,404);
   }

   @Test
    public void deleteUserWithOtherId(){
       //Create user
       UserAuthTest userAuthTest = new UserAuthTest();
       Map<String,String>data = DateGenerator.getRegistrationData();
       JsonPath responseUserReg = RestAssured
               .given()
               .body(data)
               .post(urlReg)
               .jsonPath();
       String userID = responseUserReg.getString("id");

       //Login user
       userAuthTest.loginUser(data.get("email"));//=>reload loginUser method



       //create another user
       Map<String,String>data1 = DateGenerator.getRegistrationData();
       JsonPath responseUserReg1 = RestAssured
               .given()
               .body(data1)
               .post(urlReg)
               .jsonPath();
       String newUserID = responseUserReg1.getString("id");
       System.out.println("New Id  " + newUserID);


       //Delete created user
       Response responseDeletedUser =RestAssured
               .given()
               .header("x-csrf-token",userAuthTest.header)
               .cookie("auth_sid",userAuthTest.cookie)
               .delete(urlReg+newUserID)
               .andReturn();

       //Check
       Response getDeletedUserInfo = RestAssured
               .get(urlReg + newUserID)
               .andReturn();
       System.out.println(getDeletedUserInfo.statusCode());
          System.out.println(getDeletedUserInfo.asString());
          Assertions.assertResponseCodeEquals(responseDeletedUser,400);
          Assertions.assertJsonByName(responseDeletedUser,"error",
                  "This user can only delete their own account.");
          Assertions.assertJsonHasNotValues(getDeletedUserInfo,fields);
   }

}
