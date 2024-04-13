import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Homework2Junit {

    @Test
    public void helloWorldWithoutName(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        assertEquals("Hello, someone",response.getString("answer"),"Unexpected result");
    }
}
