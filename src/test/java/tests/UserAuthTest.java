package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.ApiCoreRequest;
import lib.BaseTestCase;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import lib.Assertions;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthTest extends BaseTestCase {
    String cookie;
    String header;
    Integer user_id;
private final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @BeforeEach
    public void loginUser() {


        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseGetAuth = apiCoreRequest
                .makePostRequest(urlLogin,authData);
        this.cookie =  this.getCookie(responseGetAuth,"auth_sid");
        this.header =  this.getHeader(responseGetAuth,"x-csrf-token");
        this.user_id = this.getIntFromResponse(responseGetAuth,"user_id");



    };
    @Test
    public void loginUser1() {


        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                /*.auth()
                .preemptive()
                .basic()*/
                 .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
      responseGetAuth.print();
        assertEquals(responseGetAuth.statusCode(), 200);

    };

    public void loginUser(String email) {


        Map<String, String> authData = new HashMap<>();
        authData.put("email",email);
        authData.put("password","123");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        this.cookie =  this.getCookie(responseGetAuth,"auth_sid");
        this.header =  this.getHeader(responseGetAuth,"x-csrf-token");
        this.user_id = this.getIntFromResponse(responseGetAuth,"user_id");



    };


    @Test
     public void testAuthUser(){



        Response responseCheckAuth = RestAssured
                .given()
                // .header("x-csrf-token",responseGetAuth.getHeader("x-csrf-token"))
                .header("x-csrf-token",this.header)
                // .cookie("auth_sid",responseGetAuth.getCookie("auth_sid"))
                .cookie("auth_sid",this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

       Assertions.assertJsonByName(responseCheckAuth,"user_id",this.user_id);
    }


    @ParameterizedTest
    @ValueSource(strings = {"cookie","header"})
    public void loginWithoutCookieOrHeader(String condition){

        RequestSpecification spec = RestAssured
                .given()
                .baseUri("https://playground.learnqa.ru/api/user/auth");
        if (condition.equals("cookie")) {
            spec.cookie("auth_sid",this.cookie);

        }else if(condition.equals("header")){

            spec.header("x-csrf-token",this.header);
        } else {
            throw new IllegalArgumentException("Condition value is  "+ condition);
        }
        Response responseForCheck = spec.get().andReturn();
        Assertions.assertJsonByName(responseForCheck,"user_id",0);


    }
}


