import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {
    @Test
    public void testJsonPath() {

        HashMap<String, String> params = new HashMap<>();
        params.put("name", "Santa");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        response.prettyPrint();
        String answer = response.getString("answer");

        if (answer == null) {
            System.out.println("Key 'answer' is absent");
        } else {
            System.out.println(answer);
        }

    }

    @Test
    public void getText() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
        String s = "String@example.com";
        String s1 = "String example string";
     //   System.out.println(s1.replace(" ",""));
     //   System.out.println(s.indexOf("@"));
     //   System.out.println(s.replace("@", ""));
     //    System.out.println(s.substring(6,7));
        System.out.println(s.charAt(6));
       int count = s.indexOf("@");
        System.out.println(s.substring(s.indexOf("@")));
        System.out.println(s.toLowerCase().contains("string"));

    }
}