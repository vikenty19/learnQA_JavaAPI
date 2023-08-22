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

    @Test
    public void loginExistingEmailUser() {

        String email = "vinkotov@example.com";
        Map<String, String> userDate = new HashMap<>();
        userDate.put("email", email);
        userDate = DataGenerator.getRegistrationData(userDate);
  /*      userDate.put("password", "123");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "learnqa");
        userDate.put("lastName", "learnqa");*/
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
        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());
        //    Assertions.assertResponseTextEquals(responseCreateAuth,"Users with email '" + email + "' already exists");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertResponseHasField(responseCreateAuth, "id");

    }

    @Test
    public void loginWrongEmailUser() {
        String email = "vinkotovexample.ru";
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

    public void loginWithOneEmptyField(String field) {
        String emptyField = "";
        Map<String, String> loginData = new HashMap<>();
          loginData.put(field,emptyField);
          loginData= DataGenerator.getRegistrationData(loginData);
        Response responseEmptyFieldUser = RestAssured
                .given()
                .body(loginData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
       Response responseCreateAuth = RestAssured
                .given()
                .body(loginData)
                .get("https://playground.learnqa.ru/api/user/")
                .andReturn();
        String cookie= responseCreateAuth.getCookie("id");
        System.out.println(cookie);

        System.out.println(responseEmptyFieldUser.asString());
        System.out.println(responseEmptyFieldUser.statusCode());

        Assertions.assertResponseTextEquals(responseEmptyFieldUser,"The value of '"+field+"' field is too short");
        Assertions.assertResponseCodeEquals(responseEmptyFieldUser,400);
        assertEquals(null,cookie);
        }

    }












