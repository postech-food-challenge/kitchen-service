package steps

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

//
//class StartOrderSteps {
//
//    private lateinit var response: Response
//    private var id = UUID.randomUUID()
//    @Given("The order has been paid")
//    fun the_order_has_been_paid() {
//    }
//
//    @When("I receive the order on my system")
//    fun i_receive_the_order_on_my_system() {
//        val url = "/start/$id"
//
//        val item = OrderItem(
//            "X-tudo",
//            quantity = 1,
//            toGo = true,
//            observations = "sem cebola"
//        )
//
//        response = given().body(listOf(item)).`when`().post(url)
//    }
//
//    @Then("The order should be registered and it's status should be RECEIVED")
//    fun the_order_should_be_registered_and_it_s_status_should_be_received() {
//        val jsObject = JSONObject(response.body().asPrettyString())
//        val map = jsObject.toMap()
//
//        assertTrue { map["status"]?.equals(OrderStatus.RECEIVED.name) ?: false }
//    }
//}