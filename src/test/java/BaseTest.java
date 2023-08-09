

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {

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
        System.out.println(body);
        // Get response
        //    Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString())
        //        .post("https://restful-booker.herokuapp.com/booking");

        Response response = RestAssured.given().auth().preemptive()
                .basic("admin", "password123")
                .body(body)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/5")
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
        //    Response response = RestAssured.given().contentType(ContentType.JSON).body(body.toString())
        //        .post("https://restful-booker.herokuapp.com/booking");

        Response response = RestAssured.given()
                .body(body)
                .when()
                .post("https://restful-booker.herokuapp.com/booking")
                .andReturn();

        response.prettyPrint();
        return response;
    }

}