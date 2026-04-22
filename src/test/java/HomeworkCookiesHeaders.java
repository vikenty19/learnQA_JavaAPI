import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.internal.assertion.CookieMatcher.getCookies;

public class HomeworkCookiesHeaders {
    @Test
    public void testJsonPath() {


        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        response.prettyPrint();
        System.out.println(response.getString("messages[1].message"));
    }
    @Test
    public void testStatusCode(){
        given()
                .baseUri("https://playground.learnqa.ru/api/")
                .when()
                .get("get_json_homework")
                .then()
                .statusCode(200);
    }
    @Test
    public void testJsonPath1() {


        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/show_all_headers")
                .jsonPath();
        response.prettyPrint();
        System.out.println(response.getString("result.GEOIP-CITY"));

    }
    @Test
    public void testHeaders() {


       Response response = RestAssured
                  .get("https://playground.learnqa.ru/api/show_all_headers")
                .andReturn();
       response.prettyPrint();

        String responseHeaders = response.getHeader("Keep-Alive");
        System.out.println(responseHeaders);
   /*     Response res = given()
                .get("https://playground.learnqa.ru/api/show_all_headers")
                .then()
                .extract().response();
        res.prettyPrint();
        System.out.println(res.asString());*/ //others way to get response

    }


    @Test
    public void testRestAssured() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");

        Response response = given()
                .redirects()
                .follow(false)
            //    .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        //  response.prettyPrint();
     //   Headers responseHeaders = response.getHeaders();
     //   System.out.println(responseHeaders);

        String locationHeader = response.getHeader("location");
        System.out.println(locationHeader);
    }

    @Test
    public void testLongRedirect() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode;
        int count = 0;
        do {
            Response response = given()
                    .redirects()
                    .follow(false)
                  //  .when()
                    .get(url)
                    .andReturn();
            statusCode = response.getStatusCode();
              //     System.out.println(statusCode);

            if (statusCode == 301) {
                count++;
            }

//             присваевываем новый  url

            url = response.getHeader("location");
            System.out.println(url);
        }
        while (statusCode != 200);
        System.out.println("Finally,statusCode is   " + statusCode);
        System.out.println("NUMBER OF REDIRECTS IS  " + count);
    }
    @Test
    public void testLongRedirect1() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode = 0;
        int count = 0;

       while (statusCode !=200){

            Response response = given()
                    .redirects()
                    .follow(false)
                    //  .when()
                    .get(url)
                    .andReturn();
            statusCode = response.getStatusCode();
                 System.out.println(statusCode);

                count++;
//             присваевываем новый  url
            url = response.getHeader("location");
            System.out.println(url);
           System.out.println(count);
        }

        System.out.println("Finally,statusCode is   " + statusCode);
        System.out.println("NUMBER OF REDIRECTS IS  " + count);
    }
    @Test
    public void testRestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");

        Response response = given()
                .redirects()
                .follow(false)
                //    .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
       response.print();
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);

        String locationHeader = response.getHeader("location");
        System.out.println(locationHeader);
        System.out.println(response.getHeader("Date"));
    }
    @Test
    public void testRestCookies() {
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

         Response responseAuthCookie = given()
                .body(data)
                //    .when()
                .get("https://playground.learnqa.ru/api/get_auth_cookie")
                 .andReturn() ;

      Map<String,String> responseCookies = responseAuthCookie.getCookies();

        System.out.println("\nCookies");
        System.out.println(responseCookies);

        String responseCookie = responseCookies.get("auth_cookie");

        Map<String,String >cookies = new HashMap<>();
        if(responseCookie != null){
        cookies.put("auth_cookie",responseCookie);}

         Response responseToCheckAuth = given()
                 .body(data)
                 .cookies(cookies)
                 .post("https://playground.learnqa.ru/api/check_auth_cookie")
                 .andReturn();
         responseToCheckAuth.print();
        Assert.assertTrue(responseCookie != null);
    }
}


