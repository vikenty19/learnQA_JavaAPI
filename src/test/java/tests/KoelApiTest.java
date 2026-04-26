package tests;
import io.restassured.specification.RequestSpecification;
import lib.AssertionsKoel;
import lib.AuthService;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.junit.jupiter.api.Assertions.*;

public class KoelApiTest extends BaseTestCase {
   // private  String token;
    Properties properties;
    RequestSpecification spec;
 @BeforeEach
    public void getTokenAndLogin() throws IOException {
   /*     properties = new Properties();
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
        this.token = response.jsonPath().getString("token");//we can't return token in @Before each make it in class scope

        assertTrue((this.token!=null && !this.token.isEmpty()),"Token was NOT generated");*/
        spec = RestAssured.given();
        spec.baseUri("https://qa.koel.app");
        spec.headers("Content-Type", "application/json",
                "Accept", "application/json");
        spec.header("Authorization", "Bearer " + AuthService.getToken());
    }


    @Test
    public void userAuthWithToken() throws IOException {
//just to check if @BeforeEach works correctly
  //      getTokenAndLogin();
        System.out.println(AuthService.getToken());
        Response userDataResponse = spec.get("/api/me");
        JsonPath userData= userDataResponse.jsonPath();
        userData.prettyPrint();
        int statusCode = userDataResponse.getStatusCode();
        AssertionsKoel.assertJsonByAdminRights(userDataResponse,"is_admin");
        AssertionsKoel.assertJsonById(userDataResponse,"id");
        assertEquals(statusCode, 200);

// second variant without response body
   /*     given()
                .baseUri("https://qa.koel.app")
                .headers(
                        "Accept", "application/json",
                        "Content-Type","application/json")
                .auth().oauth2(token)
                .when()
                .get("/api/me")
                .then()
                .log().body()
                .statusCode(200);*/

    }
    @Test//No rights
    public void createNewUserByRegisteredUser(){

        Map<String, String> authData = new HashMap<>();
        authData.put("email", AuthService.properties.getProperty("KoelEmail"));
        authData.put("password",AuthService.properties.getProperty("KoelPassword"));
        authData.put("name","Donald Trump");
        authData.put("is_admin","false");
        Response response = RestAssured
             .given()
             .spec(spec)
             .body(authData)
             .when()
             .post("/api/user")
                .andReturn();
               response.prettyPrint();
    }
 @Test
 public void updateCurrentUser(){

     Map<String, String> authData = new HashMap<>();
     authData.put("email", AuthService.properties.getProperty("KoelEmail"));
     authData.put("current_password", AuthService.properties.getProperty("KoelPassword"));
     authData.put("name",AuthService.properties.getProperty("name"));
     Response response = RestAssured
             .given()
             .spec(spec)
             .body(authData)
             .when()
             .put("/api/me")
             .andReturn();
     assertEquals(200,response.getStatusCode());
     //check changes
     JsonPath responseForCheck =RestAssured
             .given()
             .spec(spec)
             .get("api/me")
             .jsonPath();
     System.out.println(responseForCheck.getString("name"));
     assertEquals(responseForCheck.getString("name"),AuthService.properties.getProperty("name"),"Name didn't change correctly");

 }
 @Test//doesn't allow
 public void updateUserData(){
     Map<String, String> authData = new HashMap<>();
     authData.put("email", properties.getProperty("KoelEmail"));
     authData.put("current_password", properties.getProperty("KoelPassword"));
     authData.put("name","John Doe");
     Response response = RestAssured
             .given()
             .spec(spec)
             .body(authData)
             .when()
             .patch("/api/user/29780")
             .andReturn();
     System.out.println(response.asString());
 }
 @Test
public void getPlayedSongs(){
     Response userData = RestAssured
             .given()
             .spec(spec).get("/api/interaction/recently-played/2")
             .andReturn();
  //     System.out.println(userData.jsonPath().prettyPrint());
//     List<String> songsId = List.of(
//             "58f8220aff6a9cf16cd22dc78bde53d7",
//             "0b794968a26cd03bb533762affc8c0ca"
//              );
     List<String> songsId = userData.jsonPath().getList("", String.class);
     System.out.println("Songs IDs: " + songsId);
     assertFalse(songsId.isEmpty(), "Songs list should not be empty");
     Response response= RestAssured
             .given()
             .spec(spec)
             .queryParams("songs",songsId)
             .when()
             .get("/api/download/songs")
             .andReturn();
     response.prettyPrint();
     assertEquals(response.getStatusCode(),404,"Unexpected status code");
}
    @Test
    public void likeSongFromResentlyPlayed() {
        Response userData = RestAssured
                .given()
                .spec(spec).get("/api/interaction/recently-played/2")
                .andReturn();
     //   System.out.println(userData.jsonPath().prettyPrint());
        String songName = userData.jsonPath().getString("[0]");
        System.out.println(songName);
      JsonPath likeSong =RestAssured
                .given()
                .spec(spec)
                .body(songName)
                .when()
                .post("/api/interaction/like")
                .jsonPath();
        System.out.println(likeSong.prettyPrint());

    }

    @Test
    public void koelJsonSchemaValidation() throws IOException {
        File file = new File("./resources/koelUserJsonSchema.json");
        //without creating response
                given()
                .baseUri(properties.getProperty("KoelURL"))
                .headers(
                        "Accept", "application/json",
                        "Content-Type", "application/json")
                .auth().oauth2(AuthService.getToken())
                .when()
                .get("/api/me")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchema(file));


    }

    @Test
    public void koelScemaValidation() {
        File file = new File("./resources/koelUserJsonSchema.json");

                given()
                .spec(spec).get("/api/me")
                .then()
                .statusCode(200)
                .body(matchesJsonSchema(file));

    }

}
