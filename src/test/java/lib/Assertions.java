package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

  public static void assertJsonByName(Response response,String name,int expectedValue){
      response.then().body("$",hasKey(name));
      int value = response.jsonPath().getInt(name);
      assertEquals(expectedValue,value,"Json value doesn't match to expected");
  }
  public static void  assertResponseTextEquals(Response response,String expectedAnswer){
      assertEquals(expectedAnswer,response.asString(),"Response IS NOT expected");
  }
    public static void  assertResponseCodeEquals(Response response,int expectedAnswer) {
        assertEquals(expectedAnswer,response.statusCode());
    }
    public static void assertResponseHasField(Response response, String expectedFieldName){
      response.then().assertThat().body("$", hasKey(expectedFieldName));
    }
    public static void assertResponseHasFields(Response response,String[]expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
          assertResponseHasField(response, expectedFieldName);
        }
    }
    public static void assertResponseHasNotField(Response response, String expectedFieldName){
        response.then().assertThat().body("$", not(hasKey(expectedFieldName)));
    }


}
