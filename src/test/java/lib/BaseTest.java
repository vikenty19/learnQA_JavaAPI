package lib;


import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Before;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTest {
    protected String getHeader(Response Response,String name) {
       Headers headers = Response.getHeaders();
       assertTrue(headers.hasHeaderWithName(name),"headers doesn't have header with name" + name);
       return headers.getValue(name);
    }
    protected String getCookie(Response Response,String name){

        Map<String,String> cookies = Response.getCookies();
        assertTrue(cookies.containsKey(name),"cookies doesn't have cookies with name" + name);
        return cookies.get(name);
    }
    protected int getIntFromJson(Response Response,String name){
       Response.then().assertThat().body("$",hasKey((name)));
       return Response.jsonPath().getInt(name);
    }
}
