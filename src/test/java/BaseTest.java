

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected RequestSpecification spec;
    @Before
 public void setUri(){
        spec = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .build();

    }
    protected Response updateBooking() {
        Map<String, Object> body = new HashMap<>();
        body.put("firstname", "Dmitry");
        body.put("lastname", "Shyshkin");
        body.put("totalprice", 150);
        body.put("depositpaid", false);

        Map<String, String> bookingdates = new HashMap<>();
        bookingdates.put("checkin", "2020-03-25");
        bookingdates.put("checkout", "2020-03-27");
        body.put("bookingdates", bookingdates);
        body.put("additionalneeds", "Baby crib");

        // Get response
        //    Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString())
        //        .post("https://restful-booker.herokuapp.com/booking");

        Response response = RestAssured.given(spec).auth().preemptive()
                .basic("admin", "password123")
                .body(body.toString())
                .when()
                .patch("/booking/5")
                .andReturn();
        response.prettyPrint();
        return response;
    }

    protected Response createBooking() {
        Map<String, Object> body = new HashMap<>();
        body.put("firstname", "Dmitry");
        body.put("lastname", "Shyshkin");
        body.put("totalprice", 150);
        body.put("depositpaid", false);

        Map<String, String> bookingdates = new HashMap<>();
        bookingdates.put("checkin", "2020-03-25");
        bookingdates.put("checkout", "2020-03-27");
        body.put("bookingdates", bookingdates);
        body.put("additionalneeds", "Baby crib");
        System.out.println(body);
        // Get response
     //     Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString())
      //
        Response response = RestAssured.given(spec)
                .body(body)
                .when()
                .post("/booking")
                .andReturn();

        response.prettyPrint();
        int code =response.getStatusCode();
        System.out.println(code);
        return response;
    }

 /*   protected Response createBooking() {
        // Create JSON body
        JSONObject body = new JSONObject();
        body.put("firstname", "Dmitry");
        body.put("lastname", "Shyshkin");
        body.put("totalprice", 150);
        body.put("depositpaid", false);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2020-03-25");
        bookingdates.put("checkout", "2020-03-27");
        body.put("bookingdates", bookingdates);
        body.put("additionalneeds", "Baby crib");

        // Get response
        Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString())
                .post("https://restful-booker.herokuapp.com/booking");
        return response;*/
}