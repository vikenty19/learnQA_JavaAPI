package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void assertJsonByName(Response response ,String name, Integer expectedValue){
        response.then().assertThat().body("$",hasKey(name));
        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue,value,"JSON value isn't equals expected value");
    }
    public static void assertJsonByName(Response response ,String name, String expectedValue){
        response.then().assertThat().body("$",hasKey(name));
        String value = response.jsonPath().getString(name);
        assertEquals(expectedValue,value,"JSON value isn't equals expected value");
    }
    public static void assertResponseTextEquals(Response response,String text){
        assertEquals(text,response.asString(),"Response text is not as expected");

    }
    public static void assertResponseCodeEquals(Response response,int expStatusCode){
        assertEquals(expStatusCode,response.statusCode(),"Response code is not as expected");

    }
    public static void assertJsonHasValue(Response response,String expectedFieldName){

       response.then().assertThat().body("$",hasKey(expectedFieldName));
    }
    public static void assertJsonHasValues(Response response,String[] expectedFieldNames){
        for(String unexpectedFieldName:expectedFieldNames){
            response.then().assertThat().body("$",hasKey(unexpectedFieldName));
        }
    }
    public static void assertJsonHasNotValues(Response response,String[] unexpectedFieldNames){
        for(String unexpectedFieldName:unexpectedFieldNames){
        response.then().assertThat().body("$",not(hasKey(unexpectedFieldName)));
        }
    }

    public static void assertJsonHasNotValue(Response response,String unExpectedFieldName){

        response.then().assertThat().body("$",hasKey(unExpectedFieldName));
    }
}
