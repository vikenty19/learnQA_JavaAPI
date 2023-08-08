import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import static io.restassured.RestAssured.given;

public class RestTraining {
    @Test
    public void restAssuredTest() {
        given()
                .when()
                .get("https://restful-booker.herokuapp.com/ping")
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    public void getBookingIdsTests() {
        Response response = RestAssured
                .get("https://restful-booker.herokuapp.com/booking")
                .andReturn();
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        List<Integer> bookingId = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingId.isEmpty());
        //   System.out.println(bookingId);
    }

    @Test
    public void getBookingName() {
        Response response = RestAssured
                .get("https://restful-booker.herokuapp.com/booking/5")
                .andReturn();
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 200);
        String bookingFirstName = response.jsonPath().getString("firstname");
        String bookingLastName = response.jsonPath().getString("lastname");
        System.out.print(bookingFirstName +  "   last name   " + bookingLastName);
        Assert.assertEquals(bookingFirstName,"Susan","doesn't match");


    }
}