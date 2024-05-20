package steps

import io.cucumber.java.Before
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType


class Config {
    @Before
    fun setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/v1/kitchen"

        RestAssured.requestSpecification = RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .build()
    }
}