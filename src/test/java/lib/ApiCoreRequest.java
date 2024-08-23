package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequest {
    @Step("Make Get-request with token and auth cookie")
    public Response makeGetRequest(String url,String token,String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token",token))
                .cookie("auth_sid",cookie)
                .get(url)
                .andReturn();

    }
    @Step("Make Get-request with  auth cookie only")
    public Response makeGetRequestWithCookie(String url,String cookie){
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid",cookie)
                .get(url)
                .andReturn();

    }

    @Step("Make Get-request with token only ")
    public Response makeGetRequestWithToken(String url,String token){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token",token))
                .get(url)
                .andReturn();

    }
    @Step("Make Post-request with token and auth cookie")
    public Response  makePostRequest(String url, Map<String,String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

    }
    @Step("Make Post request with email and password")
    public Response makePostRequestCreateUser(String url,Map<String,String>authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

    }
   @Step("Make post request without token and cookie")
    public Response makePostRequestUnauthorized(String url,Map<String,String>authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

   }
}

