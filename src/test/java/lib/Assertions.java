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
    public static void assertJsonByName(Response response,String name,String expectedValue){
        response.then().body("$",hasKey(name));
        String value = response.jsonPath().getString(name);
        assertEquals(expectedValue,value,"Json value doesn't match to expected");
    }
  public static void  assertResponseTextEquals(Response Response,String expectedAnswer){
      assertEquals(expectedAnswer,Response.asString(),"Response IS NOT expected");
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
    // Написаны для практики для userGetTest
 public static void assertJsonHasKey1(Response Response, String expectedFieldName){
      Response.then().assertThat().body("$",hasKey(expectedFieldName));

 }
public static void assertJsonHasNotKey1(Response response, String unexpectedFieldName){
      response.then().assertThat().body("$",not(hasKey(unexpectedFieldName)));
}
public static void assertResponseHasKeys(Response response,String[] expectedFieldNames){
      for(String expectedFieldName : expectedFieldNames){
         Assertions.assertJsonHasKey1(response,expectedFieldName);
          System.out.println(expectedFieldName);
      }

}
}
