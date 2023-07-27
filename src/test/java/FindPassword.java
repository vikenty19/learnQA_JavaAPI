import groovyjarjarpicocli.CommandLine;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FindPassword {
//    @CommandLine.Parameters
    //  public static Collection<Object[]> data() {
    //  Object[][] data = new Object[][]{{1}, {2}, {3}, {4}};
    //    return Arrays.asList(data);

    //  }


    @Test
    public void findPassword() {

        HashMap<String, String> date = new HashMap<>();
        date.put("login", "super_admin");
        date.put("password", "NY");

      //  while ( true)
            do {
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
                if (password == false) break;
            } while (true);
        System.out.println(date);
    }

}

