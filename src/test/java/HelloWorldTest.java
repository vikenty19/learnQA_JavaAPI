import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {
    @Test
    public void testJsonPath(){

        HashMap<String,String> params = new HashMap<>();
        params.put("name","Santa");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
                 response.prettyPrint();
        String answer = response.get("answer");

        if (answer == null){
            System.out.println("Key 'answer' is absent");
        } else {
            System.out.println(answer);
        }

    }
    @Test
    public void getText(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.print();

    }
    @Test
    public void getTextJson(){
       JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json")
                .jsonPath();
        response.prettyPrint();

    }

}
