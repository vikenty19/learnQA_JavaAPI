import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {
    @Test
    public void testRestAssured(){
       // System.out.println("Hello World!");
     //   Map<String,String> params = new HashMap<>();
        Response response = RestAssured
                .given()
                .queryParam("name","Santa")
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();

    }
    @Test
    public void getText(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();

    }
}
