package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTest;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserLoginTest  extends BaseTest {

    @Test
    public void loginExistingEmailUser(){

        String email = "vinkotov@example.com";
        Map<String,String> userDate = new HashMap<>();
        userDate.put("email",email );
        userDate.put("password", "123");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "learnqa");
        userDate.put("lastName", "learnqa");
        Response responseCreateAuth = RestAssured
                .given()
                .body(userDate)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        System.out.println(responseCreateAuth.asString());
        System.out.println(responseCreateAuth.statusCode());
        Assertions.assertResponseTextEquals(responseCreateAuth,"Users with email '" + email + "' already exists");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }
    @Test
    public void loginNewEmailUser(){

        String email = DataGenerator.getRandoEmail();
        Map<String,String> userDate = new HashMap<>();
        userDate.put("email",email );
        userDate.put("password", "123");
        userDate.put("username", "learnqa");
        userDate.put("firstName", "learnqa");
        userDate.put("lastName", "learnqa");
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

}










