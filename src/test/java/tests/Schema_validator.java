package tests;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lib.AuthService;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import java.io.File;
import java.io.IOException;

public class Schema_validator  {
    RequestSpecification spec;

@Test
    public void jsonLearnQAValidator() throws IOException {

  spec = createSpecWithToken();
        File file= new File("./src/main/resources/json_schema-auth.json");
        given()
                .spec(spec)
                .when()
                .get("/api/me")
                .then()
                .assertThat()
                .body(matchesJsonSchema(file));
    }
    public RequestSpecification createSpecWithToken() throws IOException {
         spec = RestAssured.given();
        spec.baseUri("https://qa.koel.app");
        spec.headers("Content-Type", "application/json",
                "Accept", "application/json");
        spec.header("Authorization", "Bearer " + AuthService.getToken());
        return spec;
    }
}