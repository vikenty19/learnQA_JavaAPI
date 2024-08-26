package lib;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
   protected String[]fields = {"firstName","lastName","email"};
    protected String[]fieldsForAuth= {"username","firstName","lastName","email"};
    protected String[]fieldsNotAuth ={"email","password","firstName","lastName"};
    protected static String[]keys = {"email","password","username","firstName","lastName"};
    protected String urlAuth = "https://playground.learnqa.ru/api/user/auth";
    protected String urlLogin = "https://playground.learnqa.ru/api/user/login";
    protected String urlReg = "https://playground.learnqa.ru/api/user/";
    protected RequestSpecification spec;
   public RequestSpecification setUpSpec(){
        spec=new RequestSpecBuilder()
                .setBaseUri(urlAuth)
                .build();
 return this.spec;
    }

/*    @BeforeEach
    public void setUpSpec() {
        spec1 = new RequestSpecBuilder()
                .setBaseUri(urlAuth)
                .build();
    }*/
    protected String getHeader(Response Response,String name){
        Headers headers = Response.getHeaders();
        assertTrue(headers.hasHeaderWithName(name),"Response doesn't have header with name  "+ name);
        return headers.getValue(name);
    }

    protected String getCookie(Response response,String name){
        Map<String,String> cookies =response.getCookies();
        assertTrue(cookies.containsKey(name),"Response doesn't have cookie with name  "+ name);
        return cookies.get(name);

    }
    protected Integer getIntFromResponse(Response response , String name){
        int userId = response.jsonPath().getInt(name);
        response.then().assertThat().body("$",hasKey(name));
        assertTrue(userId > 0);
        System.out.println("user id  is   "+ userId);
        return userId;
    }
}
