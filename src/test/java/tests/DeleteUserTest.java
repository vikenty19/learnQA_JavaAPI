package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequest;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class DeleteUserTest extends BaseTestCase {
    protected final ApiCoreRequest apiCoreRequest = new ApiCoreRequest();
    @Test
    public void testDeleteProtectedUser(){
        Map<String,String>data = new HashMap<>();
        data.put("email","vinkotov@example.com");
        data.put("password","1234");
        String URL = urlReg + "/2";
               Response responseToDelete = apiCoreRequest
                .deleteProtectedUser(URL,data);
      /*  Response responseToDelete = RestAssured
                .given()
                .body(data)
                .delete(urlReg + "/1")
                .andReturn();*/
          responseToDelete.jsonPath().prettyPrint();

        Assertions.assertResponseCodeEquals(responseToDelete,400);
        Assertions.assertJsonHasValue(responseToDelete,"error");


    }


}
