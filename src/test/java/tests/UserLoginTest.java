package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTest;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserLoginTest extends BaseTest {
    @Test//made by me
    public void loginAlreadyRegisteredUser(){
        String email = "vinkotov@example.com";
        Map<String, String> userDate = new HashMap<>();
        userDate.put("email",email);
        userDate.put("password","123");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "learnqa");
        userDate.put("lastName", "learnqa");
        Response responseAlreadyLogin = RestAssured
                .given()
                .body(userDate)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println(responseAlreadyLogin.asString());// == responseAlreadyLogin.print();
        System.out.println(responseAlreadyLogin.getStatusCode());//==System.out.println(responseAlreadyLogin.statusCode());
        Assertions.assertResponseTextEquals(responseAlreadyLogin,"Users with email '" + email + "' already exists");
        Assertions.assertResponseCodeEquals(responseAlreadyLogin,400);

    }

    @Test
    public void loginExistingEmailUser() {

       String email = "vinkotov@example.com";
        Map<String, String> userDate = new HashMap<>();
        userDate.put("email", email);
        userDate = DataGenerator.getRegistrationData(userDate);
          System.out.println(userDate);
        Response responseCreateAuth = RestAssured
                .given()
                .body(userDate)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Test
    public void loginNewEmailUser() {

        //    String email = DataGenerator.getRandoEmail();
        //   Map<String,String> userDate = new HashMap<>();
        Map<String, String> userDate = DataGenerator.getRegistrationData();

     /*   userDate.put("email",email );
        userDate.put("password", "123");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "learnqa");
        userDate.put("lastName", "learnqa");*/
        Response responseCreateAuth = RestAssured
                .given()
                .body(userDate)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
     //   System.out.println(responseCreateAuth.print());
        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());
        //    Assertions.assertResponseTextEquals(responseCreateAuth,"Users with email '" + email + "' already exists");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertResponseHasField(responseCreateAuth, "id");

    }

    @ParameterizedTest
    @ValueSource(strings = {"vinkotovexample.ru","    ","55555555"})
    public void loginWrongEmailUser(String email) {
      //  String email = "vinkotovexample.ru";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);
        Response responseWrongEmailUser = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println(responseWrongEmailUser.asString());
        System.out.println(responseWrongEmailUser.statusCode());
        Assertions.assertResponseTextEquals(responseWrongEmailUser, "Invalid email format");
        Assertions.assertResponseCodeEquals(responseWrongEmailUser, 400);

    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})

    public void authWithOneEmptyField(String field) {
        String emptyField = null;
        System.out.println(emptyField);
        Map<String, String> loginData = new HashMap<>();
          loginData.put(field,emptyField);
          loginData= DataGenerator.getRegistrationData(loginData);
          // CREATE USER WITH EMPTY FIELD
        Response responseEmptyFieldUser = RestAssured
                .given()
                .body(loginData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        //CHECK REGISTRATION
      Response responseCreateAuth = RestAssured
            //    .given()
              //  .body(loginData)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();
        System.out.println(responseCreateAuth.asString());
        int userId = responseCreateAuth.jsonPath().getInt("user_id");
        System.out.println(responseEmptyFieldUser.asString());
       System.out.println(responseEmptyFieldUser.statusCode());

        Assertions.assertResponseTextEquals(responseEmptyFieldUser,"The following required params are missed: "+field);
        Assertions.assertResponseCodeEquals(responseEmptyFieldUser,400);
       assertEquals(0,userId);
        }
       @Test
    public void loginWithIncorrectEmail(){


       }
    }












