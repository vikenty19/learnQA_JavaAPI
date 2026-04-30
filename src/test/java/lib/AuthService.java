package lib;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthService {
    private static String token;
    public static Properties properties;
    public static String getToken() throws IOException {
        properties = new Properties();
        File data = new File("./src/test/java/lib/properties");
        FileInputStream loadData = new FileInputStream(data);
        properties.load(loadData);
        Map<String, String> authData = new HashMap<>();
        authData.put("email", properties.getProperty("KoelEmail"));
        authData.put("password", properties.getProperty("KoelPassword"));
        Response response = RestAssured
                .given()
                .baseUri(properties.getProperty("KoelURL"))
                .headers("Content-Type", "application/json",
                        "Accept", "application/json")
                .body(authData)
                .when()
                .post("/api/me")
                .thenReturn();
            token = response.jsonPath().getString("token");
          assertTrue((token!=null && !token.isEmpty()),"Token was NOT generated");
        return token;
    }
    public static Properties readProperties() throws IOException {
        properties = new Properties();
        File data = new File("./src/test/java/lib/properties");
        FileInputStream loadData = new FileInputStream(data);
        properties.load(loadData);
        return properties;
    }
}
