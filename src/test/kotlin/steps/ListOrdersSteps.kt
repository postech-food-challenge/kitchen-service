package steps

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.response.Response
import org.json.JSONArray
import org.json.JSONObject
import kotlin.test.assertTrue

class ListOrdersSteps {

    private var currentStatusOnRequest: String? = null
    private lateinit var response: Response

    @Given("I have a few orders on my database")
    fun i_have_a_few_orders_on_my_database() {
    }

    @When("I try to find orders on status = {word}")
    fun i_try_to_find_orders_on_status(status: String) {
        currentStatusOnRequest = status
        response = RestAssured.given()
            .queryParam("status", status)
            .`when`()
            .get()
    }

    @Then("The system should warn me that the status is invalid2")
    fun the_system_should_throw_me_an_error2() {
        val jsObject = JSONObject(response.body().asPrettyString())
        val map = jsObject.toMap()

        assertTrue { map.containsValue("Invalid status: $currentStatusOnRequest") }
    }

    @When("I try to find orders without status filter")
    fun i_try_to_find_orders_without_status_filter() {
        currentStatusOnRequest = null
        response =
            RestAssured.get()
    }

    @Then("The system should return all orders whose status aren't on the list")
    fun the_system_should_return_all_orders_whose_status_aren_t_on_the_list(statusInativos: List<String>) {
        val jsArray = JSONArray(response.body().asPrettyString())

        val count = (0 until jsArray.length())
            .map { jsArray.getJSONObject(it) }
            .count { it.optString("status") in statusInativos }

        assertTrue { count == 0 }
    }

    @Then("The system should return all orders on {word} status")
    fun the_system_should_return_all_orders_on_status(status: String) {
        val jsArray = JSONArray(response.body().asPrettyString())

        val count = (0 until jsArray.length())
            .map { jsArray.getJSONObject(it) }
            .count { it.optString("status") == status }

        assertTrue { jsArray.length() == count }
    }

}