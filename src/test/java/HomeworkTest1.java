import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HomeworkTest1 {
    @Test
    public void testJsonPath() {


        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        response.prettyPrint();
        System.out.println(response.getString("messages[1].message"));
    }

    @Test
    public void testRestAssured() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");

        Response response = RestAssured
                .given()
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
            Response response = RestAssured
                    .given()
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
    public void testRestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");

        Response response = RestAssured
                .given()
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

         Response responseAuthCookie = RestAssured
                .given()
                .body(data)
                //    .when()
                .get("https://playground.learnqa.ru/api/get_auth_cookie")
                 .andReturn() ;

      Map<String,String> responseCookies = responseAuthCookie.getCookies();

        System.out.println("\nCookies");
        System.out.println(responseCookies);
        System.out.println(responseCookies.get("auth_cookie"));

        String responseCookie = responseCookies.get("auth_cookie");

        Map<String,String >cookies = new HashMap<>();
        if(responseCookie != null){
        cookies.put("auth_cookie",responseCookie);}
         Response responseToCheckAuth = RestAssured
                 .given()
                 .body(data)
                 .cookies(cookies)
                 .post("https://playground.learnqa.ru/api/check_auth_cookie")
                 .andReturn();
         responseToCheckAuth.print();
    }

}
