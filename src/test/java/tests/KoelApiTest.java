package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class KoelApiTest {
private static String token;
@Test
    public void  userAuthWithToken(){
        //getting token

        Map<String,String> body = new HashMap<>();
        body.put("email","vikenty.plakhov@testpro.io");
        body.put("password","MEGAdelta06@");
        Response response= RestAssured
                .given()
                .baseUri("https://qa.koel.app")
                .headers( "Content-Type","application/json",
                        "Accept", "application/json")
                .body(body)
                .when()
                .post("/api/me")
                .thenReturn();
       token = response.jsonPath().getString("token");
        System.out.println(token);
        //get user data
        // first variant with response
   /*     Response userData = RestAssured
                .given()
                .baseUri("https://qa.koel.app")
                .headers(
                 "Accept", "application/json",
                "Content-Type","application/json")
                .header("Authorization","Bearer "+token)
                .get("/api/me")
                .andReturn();
       String use_id = userData.jsonPath().getString("id");
        System.out.println(use_id);*/

// second variant without response body
        given()
                .baseUri("https://qa.koel.app")
                .headers(
                        "Accept", "application/json",
                        "Content-Type","application/json")
                .auth().oauth2(token)
                .when()
                .get("/api/me")
                .then()
                .log().body()
                .statusCode(200);

    }
@Test
    public void likeSong(){
  //  token=getToken();//commented to check if we have token when running whole class--> yes!
    System.out.println(token);
   Response userData = RestAssured
            .given()
            .baseUri("https://qa.koel.app")
            .headers(
                    "Accept", "application/json",
                    "Content-Type","application/json")
            .header("Authorization","Bearer "+token)
            .get("/api/interaction/recently-played/2")
            .andReturn();
    System.out.println(userData.asString());

}
@Test
    public void  koelJsonSchemaValidation(){
    File file = new File("./resources/koelUserJsonSchema.json");
  //  token = getToken();
    given()
            .baseUri("https://qa.koel.app")
            .headers(
                    "Accept", "application/json",
                    "Content-Type","application/json")
            .auth().oauth2(token)
            .when()
            .get("/api/me")
            .then()
            .log().all()
            .statusCode(200)
            .body(matchesJsonSchema(file));
}

public String getToken(){
    Map<String,String> body = new HashMap<>();
    body.put("email","vikenty.plakhov@testpro.io");
    body.put("password","MEGAdelta06@");
    Response response= RestAssured
            .given()
            .baseUri("https://qa.koel.app")
            .headers( "Content-Type","application/json",
                    "Accept", "application/json")
            .body(body)
            .when()
            .post("/api/me")
            .thenReturn();
    return token = response.jsonPath().getString("token");
}
}
