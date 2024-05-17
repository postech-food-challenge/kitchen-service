package steps

import io.cucumber.java.Before
import io.ktor.server.engine.*
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.http.ContentType


class Config {
//    private val userApi: UserApi
//    private val petApi: PetApi
//
//    init {
//        userApi = UserApi()
//        petApi = PetApi()
//    }

    @Before
    fun setup() {


        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

//        val properties: ServerConfig = ConfigManager.getConfiguration()

//        RestAssured.baseURI = java.lang.String.format("%s:%d", properties.baseURI(), properties.port())
//        RestAssured.basePath = properties.basePath()

        RestAssured.baseURI = "http://localhost:8080"
        RestAssured.basePath = "/kitchen"

        RestAssured.requestSpecification = RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .build()
    }
}