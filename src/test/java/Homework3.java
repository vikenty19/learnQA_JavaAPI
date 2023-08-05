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


   @ParameterizedTest
   @ValueSource(strings = {"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"})
    public void checkUserAgent(String name){
        Map<String,String> headers = new HashMap<>();
        headers.put("user-agent",name);
        Response response =RestAssured
                .given()
                .headers(headers)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();
        response.prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        String platform = jsonPath.get("platform");
        String browser = jsonPath.get("browser");
        String device = jsonPath.get("device");
        if(platform.equalsIgnoreCase("Unknown")
                || (browser.equalsIgnoreCase("no"))
                 || (device.equalsIgnoreCase("Unknown"))){
            System.out.println(name);
            if(platform.equalsIgnoreCase("Unknown")){
                System.out.println("platform is unknown");
            }
            if(browser.equalsIgnoreCase("no")){
                System.out.println("browser is Unknown");
            }
            if(device.equalsIgnoreCase("Unknown")){
                System.out.println("device is Unknown");
            }
        }

   }



}

