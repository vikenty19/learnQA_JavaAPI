package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.ApiCoreRequest;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorization cases")
@Feature("Authorization")
public class AllureUserAuthTest extends BaseTestCase {
    String cookie;
    String header;
    Integer user_id;
    private final ApiCoreRequest apiCoreRequest= new ApiCoreRequest();
    @org.junit.jupiter.api.BeforeEach
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
    @Description("This test is happy pass")
    @DisplayName("Test positive auth user")
    public void testAuthUser(){

        Response responseCheckAuth = apiCoreRequest
                .makeGetRequest(urlAuth,
                        this.header,
                        this.cookie);
       Assertions.assertJsonByName(responseCheckAuth,"user_id",this.user_id);
    }

    @Description("This test checks authorization status w/o auth cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie","header"})
    public void loginWithoutCookieOrHeader(String condition){

        RequestSpecification spec = RestAssured
                .given()
                .baseUri(urlAuth);
        if (condition.equals("cookie")){
            Response responseForCheck = apiCoreRequest
                    .makeGetRequestWithCookie(urlAuth,
                            this.cookie);
            Assertions.assertJsonByName(responseForCheck,"user_id",0);
        }
       else if (condition.equals("header")){
            Response responseForCheck = apiCoreRequest
                    .makeGetRequestWithToken(urlAuth,
                            this.header);
            Assertions.assertJsonByName(responseForCheck,"user_id",0);

        }else {
           throw new IllegalArgumentException("Condition is unknown  "+ condition);
        }


            if (condition.equals("cookie")) {
            spec.cookie("auth_sid",this.cookie);

        }
             else if(condition.equals("header")){

             spec.header("x-csrf-token",this.header);
        } else {
            throw new IllegalArgumentException("Condition value is  "+ condition);
        }
        Response responseForCheck = spec.get().andReturn();
        Assertions.assertJsonByName(responseForCheck,"user_id",0);


    }
}


