package steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured.given
import io.restassured.response.Response
import org.json.JSONObject
import kotlin.test.assertTrue

class UpdateOrderStatusSteps {

    private var currentOrderId = 0
    private lateinit var currentStatusOnRequest: String
    private lateinit var response: Response

    @Given("I'm trying to update the status of an order with id = {int}")
    fun a(id: Int) {
        currentOrderId = id
    }

    @When("I try to change it's status to {word}")
    fun i_try_to_change_its_status(status: String) {
        val url = "/${currentOrderId}"
        val body = "{ \"status\": \"${status}\" }"

        currentStatusOnRequest = status
        response =
            given()
                .body(body)
                .`when`()
                .patch(url)
    }

    @Then("The system should warn me that there's no such order")
    fun b() {
        val jsObject = JSONObject(response.body().asPrettyString())
        val map = jsObject.toMap()

        assertTrue { map.containsValue("No order found for id = $currentOrderId") }
    }

    @Then("The system should warn me that the status is invalid")
    fun the_system_should_throw_me_an_error() {
        val jsObject = JSONObject(response.body().asPrettyString())
        val map = jsObject.toMap()

        assertTrue { map.containsValue("Invalid status: $currentStatusOnRequest") }
    }

    @Then("I should receive the order with it's status set to {word}")
    fun i_should_receive_the_order_with_it_s_status_set_to_canceled(expectedStatus: String) {
        val jsObject = JSONObject(response.body().asPrettyString())
        val map = jsObject.toMap()

        assertTrue { map["status"]?.equals(expectedStatus) ?: false }
    }
}