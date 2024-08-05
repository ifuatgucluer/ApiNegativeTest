
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;


public class ApiNegativeTest {

    @Test
    public void testInvalidEndpoint() {
        RestAssured.baseURI = "https://api.example.com";

        // Geçersiz bir endpoint'e istek gönder
        Response response = given()
                .when()
                .get("/invalid-endpoint")
                .then()
                .statusCode(404) // 404 Hata kodunu bekler
                .extract().response();

        // Hata mesajını kontrol et
        String errorMessage = response.jsonPath().getString("error.message");
        assertEquals("Not Found", errorMessage);
    }

    @Test
    public void testMissingRequiredField() {
        RestAssured.baseURI = "https://api.example.com";

        // Zorunlu alanın eksik olduğu bir POST isteği gönder
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{ \"name\": \"\" }") // 'name' alanı boş
                .when()
                .post("/create")
                .then()
                .statusCode(400) // 400 Hata kodunu bekler
                .extract().response();

        // Hata mesajını kontrol et
        String errorMessage = response.jsonPath().getString("error.message");
        assertEquals("Required field 'name' is missing", errorMessage);
    }

    @Test
    public void testInvalidDataFormat() {
        RestAssured.baseURI = "https://api.example.com";

        // Geçersiz veri formatı ile bir PUT isteği gönder
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{ \"age\": \"twenty\" }") // 'age' alanı sayı olmalı, metin değil
                .when()
                .put("/update")
                .then()
                .statusCode(422) // 422 Hata kodunu bekler
                .extract().response();

        // Hata mesajını kontrol et
        String errorMessage = response.jsonPath().getString("error.message");
        assertEquals("Invalid data format for field 'age'", errorMessage);
    }
}
