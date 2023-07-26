import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;

public class FindPassword {
    @Test
    public void findPassword() {

        HashMap<String, String> date = new HashMap<>();
        date.put("login", "super_admin");
         date.put("password","NY");
         Boolean equals = false;

         if(equals = false){
        Response getResponse = RestAssured
                .given()
                .body(date)
                .when()
                .post(" https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                .andReturn();
       getResponse.prettyPrint();
     String authCookie =  getResponse.getCookie("auth_Cookie");

        System.out.println(authCookie);
        int statusCode =  getResponse.getStatusCode();
        System.out.println(statusCode);


        Map<String,String> cookies = new HashMap<>();
        cookies.put("auth_Cookie",authCookie );
        Response responseForPassword = RestAssured
                .given()
                .body(date)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                .andReturn();
        responseForPassword.print();}

    }
}