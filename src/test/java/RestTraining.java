import com.fasterxml.jackson.databind.util.JSONPObject;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestTraining {
    @Test
    public void restAssuredTest() {
        given()
                .when()
                .get("https://restful-booker.herokuapp.com/ping")
                .then()
             //   .assertThat()//works without this line
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
        Assert.assertEquals("doesn't match",bookingFirstName,"Susan");


    }
    @Test
    public void findProperties(){
    Response response= RestAssured
                .get("https://cde8bdc4bb9d45de9b1fff00c4118687.api.mockbin.io/")
                .andReturn();

     String Code = response.jsonPath().getString("[0].Item01.Data[0].Results[0].Contact.Address.PostalCode");
        System.out.println(Code);
      // response[0].Item01.Data[0].Results[0].Contact.Address.PostalCode;---postman path
    }

    public static void main(String[] args) {
        System.out.println(1000+"4"+4);
        int[]num = {1,2,7,0,4,0};
        int temp;
        boolean sorted = false;
        while (!sorted){
           sorted = true;
            for (int i = 0; i < num.length -1 ; i++) {
                if (num[i]>num[i+1]){
                    temp = num[i];
                    num[i]=num[i+1];
                    num[i+1]=temp;
                    sorted =false;
                }


            }
        }
        for(int temp1:num){
        System.out.print(temp1+"");
    }
        System.out.println();
}}