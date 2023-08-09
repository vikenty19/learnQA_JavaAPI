import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.assertj.core.api.SoftAssertions;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GetBookingTests extends BaseTest {

    @Test
    public void getBookingTest() {
        // Get response with booking
        Response response = RestAssured.get("https://restful-booker.herokuapp.com/booking/5");
        response.prettyPrint();

        // Verify response 200
        Assert.assertEquals(response.getStatusCode(), 200);
        ;

        // Verify All fields
        //    SoftAssert softAssert = new SoftAssert();
        //    SoftAssertions softly = new SoftAssertions();
        SoftAssertions.assertSoftly(softly -> {
            String actualFirstName = response.jsonPath().getString("firstname");
            //         softAssert.assertEquals(actualFirstName, "Sally", "firstname in response is not expected");
            softly.assertThat(actualFirstName).isEqualTo("Mark");

        });


          /*      String actualLastName = response.jsonPath().getString("lastname");
                softAssert.assertEquals(actualLastName, "Ericsson", "lastname in response is not expected");

                int price = response.jsonPath().getInt("totalprice");
                softAssert.assertEquals(price, 753, "totalprice in response is not expected");

                boolean depositpaid = response.jsonPath().getBoolean("depositpaid");
                softAssert.assertTrue(depositpaid, "depositpaid should be true, but it's not");

                String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
                softAssert.assertEquals(actualCheckin, "2016-02-06", "checkin in response is not expected");

                String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
                softAssert.assertEquals(actualCheckout, "2016-09-27", "checkout in response is not expected");

                String actualAdditionalneeds = response.jsonPath().getString("additionalneeds");
                softAssert.assertEquals(actualAdditionalneeds, "Breakfast", "additionalneeds in response is not expected");*/

        //  softAssert.assertAll();

    }

    @Test
    //         public  class BaseTest {

    public void createNewBooking() {


        Response response = createBooking();
        response.prettyPrint();


        //    return response;

    }
    @Test
    public void updateBookingTest() {
        Response responseCreate = updateBooking();
        responseCreate.prettyPrint();
       int bookingid = responseCreate.jsonPath().getInt("bookingid");
        System.out.println(bookingid);


    }
}




