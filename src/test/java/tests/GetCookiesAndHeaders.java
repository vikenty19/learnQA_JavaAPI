package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class GetCookiesAndHeaders {
    @Test
    public void testLongRedirect() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");

        Response response = RestAssured
                .given()
                //       .headers(headers) // its doesn't have any effect on a result
                .redirects()

                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        //    response.prettyPrint();
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);

        String locationHeader = response.getHeader("location");
        System.out.println("Redirect location:  " + locationHeader);
    }

    @Test
    public void getCookies() {
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");
        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();
        Map<String, String> responseCookie = response.getCookies();
 /*   Headers responseHeader = response.getHeaders();
      System.out.println("\npretty text ");
    response.prettyPrint();
    System.out.println("\nresponse Header");
    System.out.println(responseHeader);
    System.out.println();*/
        System.out.println("response Cookie\n"+ responseCookie);
        System.out.println(responseCookie);
        String response1Cookie = response.getCookie("auth_cookie");//просто чтобы проверить разницу мар и стринг
        System.out.println(response1Cookie);

    }

    @Test
    public void getCookieAndCheckAuthorize(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");
        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

     String authCookie = responseForGet.getCookie("auth_cookie");
        Map<String,String >cookieForCheck = new HashMap<>();
        // check if credentials are valid or not
     if (authCookie != null) {
     cookieForCheck.put("auth_cookie",authCookie);}
     // pass cookie and body to authorize
        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookieForCheck)
                .when()
                .get("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

            System.out.println(cookieForCheck);
            responseForCheck.print();

    }
}
