import io.restassured.RestAssured;
import io.restassured.http.Cookies;
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
        System.out.println(response.getString("messages[0].timestamp"));
       // System.out.println(response.get("messages.get(messages").get(1).get("message"))");
    }                                  //        messages.get("messages").get(1).get("message");

    @Test
    public void testRestAssured() {
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
    public void testLongRedirect() {
        Map<String, String> headers = new HashMap<>();
        headers.put("myHeader1", "myValue1");
        headers.put("myHeader2", "myValue2");
        String locationHeader = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode;
        int count = 0;
        do {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(locationHeader)
                    .andReturn();
            statusCode = response.getStatusCode();
    //        System.out.println(statusCode);

            if(statusCode == 301){
                count++;

            }

            //  response.prettyPrint();
            Headers responseHeaders = response.getHeaders();
       //     System.out.println(responseHeaders);

            locationHeader = response.getHeader("location");
    //        System.out.println(locationHeader);
        }
        while (statusCode != 200);
        System.out.println("Finally,statusCode is   "+ statusCode);
        System.out.println("NUMBER OF REDIRECTS IS  " + count );
    }

}
