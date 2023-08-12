package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

  public static void assertJsonByName(Response response,String name,int expectedValue){
  //   response.then().body("$",hasKey(name));
      int value = response.jsonPath().getInt(name);
      assertEquals(expectedValue,value,"Json value doesn't match to expected");
  }
}
