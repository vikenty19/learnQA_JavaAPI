package lib;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    protected String[] fields = {"firstName", "lastName", "email"};
    protected String[] fieldsForAuth = {"username", "firstName", "lastName", "email"};
    protected String[] fieldsNotAuth = {"email", "password", "firstName", "lastName"};
    protected static String[] keys = {"email", "password", "username", "firstName", "lastName"};
    protected String urlAuth = "https://playground.learnqa.ru/api/user/auth";
    protected String urlLogin = "https://playground.learnqa.ru/api/user/login";
    protected String urlReg = "https://playground.learnqa.ru/api/user/";
    protected RequestSpecification spec;
    public Properties properties;

    public RequestSpecification setUpSpec() {
        spec = new RequestSpecBuilder()
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
    public Response loginRegisteredUser(){
        try {
            properties = getProperty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> authData = new HashMap<>();
        authData.put("email", properties.getProperty("email"));
        authData.put("password", properties.getProperty("password"));
        Response responseGetAuth = RestAssured.given()
                .body(authData)
                .post(urlLogin)
                .andReturn();
        return responseGetAuth;
    }
    protected String getHeader(Response response, String name) {
        Headers headers = response.getHeaders();
        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have header with name  " + name);
        return headers.getValue(name);
    }

    protected String getCookie(Response response, String name) {
        Map<String, String> cookies = response.getCookies();
        assertTrue(cookies.containsKey(name), "Response doesn't have cookie with name  " + name);
        return cookies.get(name);

    }

    protected Integer getIntFromResponse(Response response, String name) {
        int userId = response.jsonPath().getInt(name);
        response.then().assertThat().body("$", hasKey(name));
        assertTrue(userId > 0);
        System.out.println("user id  is   " + userId);
        return userId;
    }
    protected Properties getProperty() throws IOException {
      Properties  properties= new Properties();
        File data = new File("./src/test/java/lib/properties");
        FileInputStream loadData = new FileInputStream(data);
        properties.load(loadData);
        return properties;
    }
}
