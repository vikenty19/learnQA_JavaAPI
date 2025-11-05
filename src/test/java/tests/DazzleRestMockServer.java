package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;

//you must rise WireMock first C:\Users\Acer\Documents\WireMock>java -jar wiremock-standalone-3.13.1.jar --port 9999
public class DazzleRestMockServer {
    //specify request port//
    private String endPoint = "/v3.1/alpha/co";
    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI="http://localhost";
        RestAssured.port=9999;


    }
    @Test
    public void selectPortToConnect(){

      ////        .baseUri("http://localhost")
        //       .port(9999)
                when()
                .get(endPoint)
                .then()
                .log().all()
                .statusCode(200);
    }
    @Test
    public void responseTimeValidation(){
        long responseTime =  given()//given is not compulsory

                .when()
                .get(endPoint)
                .then()
                //hamcrest matcher time must be in the Long format
                .time(lessThan(3000L), TimeUnit.MILLISECONDS)
              // .log().all()
                .statusCode(200)
                .extract()//getting response time
                .time();
        System.out.println("Response time is -->" + responseTime);
    }
    @Test
     public void connectWatsUpEndPoint(){

     Response response=   when()
                .get("/whatsUp")
                .then()
               // .log().all()
                .statusCode(200)
                .extract().response();
       response.prettyPrint();
       String id = response.jsonPath().getString("id");
        System.out.println(id);
       response.then().assertThat().body("$",hasKey("id"));
     }

}
