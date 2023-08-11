package lib;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTest {
    protected String getHeader(Response response,String name) {
       Headers headers = response.getHeaders();
       assertTrue(headers.hasHeaderWithName(name),"headers doesn't have header with name" + name);
       return headers.getValue(name);
    }
    protected String getCookie(Response response,String name){

        Map<String,String> cookies = response.getCookies();
        assertTrue(cookies.containsKey(name),"cookies doesn't have cookies with name" + name);
        return cookies.get(name);
    }
}
