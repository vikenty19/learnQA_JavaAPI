package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DateGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
@Epic("Registration cases")
@Feature("Registration of user")
    @Test
    public void createUserWithExistingEmail(){
        String email = "vinkotov@example.com";
        Map<String ,String> userData = new HashMap<>();//HashMap with notDefaultValues
        userData.put("email",email);
        userData = DateGenerator.getRegistrationData(userData);
            Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(urlReg)
                .andReturn();
               System.out.println(responseCreateAuth.asString());
        Assertions.assertResponseTextEquals(responseCreateAuth,"Users with email '"+ email+"' already exists");
        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
    }
    @Test
    @Description("This is a happy path")
    @DisplayName("Registration with valid credentials")
    public void createUserSuccessfully(){

        Map<String ,String> userData = new HashMap<>();
        userData = DateGenerator.getRegistrationData();//register data has moved to DataGenerator class
        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                // just idea to transfer screenshot files
                  //      .multiPart("file",new File("C:\\Users\\Acer\\" +
                    //            "Documents\\ShareX\\Screenshots\\2023-02\\chrome_492GhAbcU9.png"))
                .post(urlReg)
                .andReturn();
        System.out.println(responseCreateAuth.asString());
         Assertions.assertResponseCodeEquals(responseCreateAuth,200);
         Assertions.assertJsonHasValue(responseCreateAuth,"id");
    }
    @Test
    @Description("Invalid email user")
    @DisplayName("user email without @")
    public void createUserIncorrectEmail(){
        String email = "vinkotovexample.com";
        Map<String ,String> userData = new HashMap<>();//HashMap with notDefaultValues
        userData.put("email",email);
        userData = DateGenerator.getRegistrationData(userData);
        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post(urlReg)
                .andReturn();
        System.out.println(responseCreateAuth.asString());
        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"Invalid email format");

    }
    @ParameterizedTest
    @CsvSource({"vinkotov@example.com, ,","  ,1234"})
    @DisplayName("negative tests")
    @Description("user tries to login with empty email or password ")
    public void createUserEmptyField(String email,String password){
        Map<String,String>authData = new HashMap<>();
        authData.put("email",email);
        authData.put("password",password);
        authData = DateGenerator.getRegistrationData(authData);
        Response responseCreateAuth = RestAssured
                .given()
                .body(authData)
                .post(urlReg)
                .andReturn();

        if (email==null){
            Assertions.assertResponseTextEquals(responseCreateAuth,
                    "The following required params are missed: email");

        } else if (password==null){
            Assertions.assertResponseTextEquals(responseCreateAuth,
                    "The following required params are missed: password");

        }
        Assertions.assertResponseCodeEquals(responseCreateAuth,400);

    }
    @ParameterizedTest
    @ValueSource(strings = {" ","q","qq","250nZa6c2gBZTtreFM2yPtHrh0JqZOGRvoCbKZgvzUWEuBUYYrmVbnPoUKYZ5aoqY8VPUemxMkEHdjHphL7Q4zYjhlQCAhkDlQ2INDc7a4TrGFEqVAAMUu4EZxvkGbsYkHK8nm6uZ6rtiyaGtdzlcNwjIJ5L" +
            "WggQeWCKKQTSAnr9IetA4xZ6DamRrisT5ehMI6u9usOImYxL2z73m0Uhrb74ouVbUPlRnksZbpOYIvOtR3TVaZu1VakC5F","251dnZa6c2gBZTtreFM2yPtHrh0JqZOGRvoCbKZgvzUWEuBUYYrmVbnPoUKYZ5aoqY8VPUemxMkEHdjHphL7Q4zYjhlQCAhkDlQ2INDc7a4TrGFEqVAAMUu4EZxvkGbsYkHK8nm6uZ6rtiyaGtdzlcNwjIJ5LWggQeWCKKQTSAnr9IetA4xZ" +
            "6DamRrisT5ehMI6u9usOImYxL2z73m0Uhrb74ouVbUPlRnksZbpOYIvOtR3TVaZu1VakC5F"})
    @Description("too long/short or empty username")
    @DisplayName("negative test for username")

    public void loginUserLongOrShortName(String name){
        Map<String,String>authData = new HashMap<>();
        authData.put("username",name);
        authData = DateGenerator.getRegistrationData(authData);
        Response responseCreateAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user")
                .andReturn();
        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());
        if(name.length()<=1){
            Assertions.assertResponseTextEquals(responseCreateAuth,
                    "The value of 'username' field is too short");
            Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        } else if (name.length()>250) {
            Assertions.assertResponseTextEquals(responseCreateAuth,
                    "The value of 'username' field is too long");
            Assertions.assertResponseCodeEquals(responseCreateAuth,400);

        }else {
            Assertions.assertResponseCodeEquals(responseCreateAuth,200);
            Assertions.assertJsonHasValue(responseCreateAuth,"id");
            Assertions.assertResponseCodeEquals(responseCreateAuth,200);
        }

    }
    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    @DisplayName("negative tests wit empty field")
    @Description("user tries to register with one needed but empty field ")
    public void negativeUserAuthWithoutOneFieldTest(String field){
        Map<String,String>regData = new HashMap<>();
        regData.put(field,null);
        regData = DateGenerator.getRegistrationData(regData);
        Response response = RestAssured.given()
                .body(regData)
                .post(urlReg)
                .andReturn();

        if(regData.get(field)==null){
            Assertions.assertResponseTextEquals(response,"The following required params are missed: "+field);
                  }
        Assertions.assertResponseCodeEquals(response,400);



    }
}