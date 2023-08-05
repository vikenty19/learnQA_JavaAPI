import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Homework3 {
    @ParameterizedTest
    @ValueSource(strings = {"1","12345","12345678910123456","vasyavasyavasya","vasyavasyavasya1"} )
    public void shortPhraseTest(String givenString){
        int length = givenString.length();
        if(length<=15){ givenString = "Name doesn't exist";}
        String answer = (length > 15) ? givenString : "Name doesn't exist";
        System.out.println(answer);
          assertEquals(givenString,answer,"Name doesn't exist");

    }

     @Test
      public void assertCookie(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Map<String,String> responseCookie = response.getCookies();
         System.out.println(responseCookie);
         String cookie = response.getCookie("HomeWork");
         assertEquals("hw_value",cookie);
     }
    @Test
    public void assertHeader(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
//        response.prettyPrint();
      Headers responseHeader = response.getHeaders();
   //     System.out.println(responseHeader);
        String  header = response.jsonPath().getString("success");
        System.out.println(header);
         assertTrue(responseHeader.hasHeaderWithName("success"));
       assertEquals("!",header);
    }

}
