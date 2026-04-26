package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;

public class AssertionsKoel {
    public static void assertJsonByAdminRights(Response response, String name) {
        response.then().assertThat().body("$", hasKey(name));
        boolean isAdmin = response.jsonPath().getBoolean("is_admin");
        assertFalse(isAdmin);
    }

    public static void assertJsonById(Response response, String id) {
        response.then().assertThat().body("$", hasKey(id));
        int user_id = response.jsonPath().getInt("id");
        assertTrue(user_id > 0, "User id should be greater then 0");

    }
}
