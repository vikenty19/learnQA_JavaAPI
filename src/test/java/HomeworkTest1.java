import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class HomeworkTest1 {
    @Test
    public void testJsonPath(){

  
         JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        response.prettyPrint();
        System.out.println(response.getString("messages[1].message"));
        }

    }



