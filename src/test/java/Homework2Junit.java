import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Homework2Junit {

    @Test
    public void helloWorldWithName(){
        String name = "Vasya";
        JsonPath response = RestAssured
                .given()
                .params("name",name)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        response.prettyPrint();
        assertEquals("Hello, " + name,response.getString("answer"),"Unexpected result");
    }
    @ParameterizedTest
    @ValueSource(strings = {"","Vasya","John","<body></body>"})
public void helloWorldWithParams(String name){
        Map<String,String> queryParams = new HashMap<>();
        if (name.length() > 0){
            queryParams.put("name",name);

        }
        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedAnswer = (name.length()>0)? name:"someone";
        System.out.println(expectedAnswer);
        assertEquals("Hello, " + expectedAnswer,answer,"Unexpected answer");
    }
}
