package steps

import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.domain.entities.OrderStatus
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured.given
import io.restassured.response.Response
import org.json.JSONObject
import java.util.*
import kotlin.test.assertTrue

//class UpdateOrderStatusSteps {
//
//    private var currentOrderId = ""
//    private var createdOrderId: UUID? = null
//    private lateinit var currentStatusOnRequest: String
//    private lateinit var response: Response
//
//    @Given("I'm trying to update the status of an order with id = {word}")
//    fun im_trying_to_update_the_status_of_an_order_with_id(id: String) {
//        currentOrderId = id
//    }
//
//    @Given("I created an order and I'm trying to update it's status")
//    fun i_created_an_order_and_im_trying_to_update_the_status_of_an_order_with_id() {
//        createdOrderId = UUID.randomUUID();
//        currentOrderId = createdOrderId.toString()
//
//        val item = OrderItem(
//            "Test item",
//            quantity = 1,
//            toGo = true,
//            observations = "sem cebola"
//        )
//
//        given().body(listOf(item)).`when`().post("/start/$createdOrderId")
//
//    }
//
//    @When("I try to change it's status to {word}")
//    fun i_try_to_change_its_status(status: String) {
//        val url = "/$currentOrderId"
//        val body = "{ \"status\": \"${status}\" }"
//
//        currentStatusOnRequest = status
//        response =
//            given()
//                .body(body)
//                .`when`()
//                .patch(url)
//    }
//
//    @Then("The system should warn me that there's no such order")
//    fun b() {
//        val jsObject = JSONObject(response.body().asPrettyString())
//        val map = jsObject.toMap()
//
//        assertTrue { map.containsValue("No order found for id = $currentOrderId") }
//    }
//
//    @Then("The system should warn me that the status is invalid")
//    fun the_system_should_throw_me_an_error() {
//        val jsObject = JSONObject(response.body().asPrettyString())
//        val map = jsObject.toMap()
//
//        assertTrue { map.containsValue("Invalid status: $currentStatusOnRequest") }
//    }
//
//    @Then("I should receive the order with it's status set to {word}")
//    fun i_should_receive_the_order_with_it_s_status_set_to_canceled(expectedStatus: String) {
//        val jsObject = JSONObject(response.body().asPrettyString())
//        val map = jsObject.toMap()
//
//        assertTrue { map["status"]?.equals(expectedStatus) ?: false }
//    }
//
//    @Then("I should receive an error stating that the id I passed is invalid")
//    fun i_should_receive_an_error_stating_that_the_id_i_passes_is_invalid() {
//        assertTrue { response.body().asPrettyString().equals("Invalid ID") ?: false }
//    }
//}