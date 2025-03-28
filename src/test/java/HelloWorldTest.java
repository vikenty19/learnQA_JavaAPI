import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTest {
    @Test

    public void testHelloWorld(){
    Map<String,String> Data = new HashMap<>();
        Data.put("name","Putin huylo");
        Data.put("name1","John");
        Response response = RestAssured
                .given()
     //           .params("name","John")    //queryParams also possible
                .params(Data)
                .get("https://playground.learnqa.ru/api/hello")
               .andReturn();
        response.prettyPrint();
        System.out.println("Status code is   :" +response.statusCode());
    }
    @Test

    public void testJsonPath() {

        HashMap<String, String> params = new HashMap<>();
        params.put("name","Santa");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        response.prettyPrint();
        String answer = response.get("answer");

        if (answer == null) {
            System.out.println("Key 'answer' is absent");
        } else {
            System.out.println(answer);
        }
    }
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
        response.print();

    }
    @Test
    public void getTextJson(){
       JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json")
                .jsonPath();
        response.prettyPrint();


    }
@Test
    public void testWithAssertions(){

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map")
                .andReturn();
        assertEquals(200,response.statusCode(), "Unexpected status code");
}
@ParameterizedTest
@ValueSource(strings = {"","Santa","John"})
    public void testHelloWorldWithName(String name){
        Map<String,String >names = new HashMap<>();
        if (name.length()>0){
            names.put("name",name);
        }


        JsonPath response= RestAssured
                .given()
                .params(names)
                .get("https://playground.learnqa.ru/api/hello")
    .jsonPath();
        String answer = response.getString("answer");
    System.out.println(answer);
    String expectedName = (name.length()>0)?name:"someone";
        assertEquals("Hello, "+expectedName,answer,"The answer is not expected");


}
}
