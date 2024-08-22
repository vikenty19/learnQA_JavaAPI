import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HomeworkToken {
    @Test
    public void testJsonToken() throws InterruptedException {

        // get a token
        JsonPath response = RestAssured
                .get(" https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        response.prettyPrint();

        // add a token

        String param = response.getString("token");
        int time = response.getInt("seconds");
        Map<String, String> headers = new HashMap<>();
        headers.put("token", param);
        JsonPath newresponse = RestAssured
                .given()
                .queryParams(headers)
                .put("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String status = newresponse.getString("status");
        System.out.println("Status  :\n" + status);
     //   newresponse.prettyPrint();

        //  waiting for task completion

        System.out.println(time);
        Thread.sleep(time*1000);
        JsonPath newresponse1 = RestAssured
                .given()
                .queryParams(headers)
                .put("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        newresponse1.prettyPrint();

    }
    @Test
    public void jobIsReady() throws InterruptedException {

        JsonPath response = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

       response.prettyPrint();

       String param = response.getString("token");
       Map<String,String>header = new HashMap<>();
       header.put("token",param);
        JsonPath responseJob = RestAssured
                .given()
                .params(header)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        responseJob.prettyPrint();
        Assertions.assertEquals("Job is NOT ready", responseJob.getString("status"));
            Thread.sleep(response.getInt("seconds")*1000);
        JsonPath responseJobFinal = RestAssured
                .given()
                .params(header)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        responseJobFinal.prettyPrint();
        Assertions.assertEquals("Job is ready", responseJobFinal.getString("status"));

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
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        //  response.prettyPrint();
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders + " ------headers");

        String locationHeader = response.getHeader("location");
        System.out.println(locationHeader + "----- location");
    }
}
