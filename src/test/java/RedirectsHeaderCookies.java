import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class RedirectsHeaderCookies {
    @Test
    public void longRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
              //  .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();
        System.out.println("Status code is   :" +response.getStatusCode());

    }

}
