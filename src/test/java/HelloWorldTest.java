import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {
    @Test
    public void testRestAssured(){

       HashMap<String,String> params = new HashMap<>();
      // params.put("name","Santa");
       params.put("name","NY");
        Response response = RestAssured
                .given()
                .queryParams(params)
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
        response.print();

    }
}
