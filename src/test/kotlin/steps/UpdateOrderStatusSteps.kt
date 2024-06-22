package steps

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.SendPatchRequestInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.infraestructure.controller.StartOrderItemRequest
import br.com.fiap.postech.infraestructure.gateways.OrderGatewayImpl
import br.com.fiap.postech.infraestructure.persistence.OrderRepository
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import java.util.*
import kotlin.test.assertTrue

class UpdateOrderStatusSteps {
    private lateinit var response: HttpResponse
    private val orderRepository = mockk<OrderRepository>(relaxed = false)
    private val sendPatchRequestInteract = mockk<SendPatchRequestInteract>(relaxed = false)
    private lateinit var mockedOrder : Order
    private var expectedReturn : Map<String, AttributeValue>? = null

    @Given("I'm trying to update the status of a nonexistent order with id = {word}")
    fun im_trying_to_update_the_status_of_a_nonexisting_order_with_id(id: String) {
        val item = StartOrderItemRequest(
            1L,
            quantity = 1,
            toGo = true,
            observations = "sem cebola",
            price = 22
        )

        mockedOrder = Order(UUID.fromString(id), listOf(OrderItem.fromRequest(item)))
        expectedReturn = null
        assertTrue { true }
    }

    @Given("I'm trying to update the status of an existing order with id = {word}")
    fun im_trying_to_update_the_status_of_an_existing_order_with_id(id: String) {
        val item = StartOrderItemRequest(
            1L,
            quantity = 1,
            toGo = true,
            observations = "sem cebola",
            price = 22
        )

        mockedOrder = Order(UUID.fromString(id), listOf(OrderItem.fromRequest(item)))
        expectedReturn = Order.toMap(mockedOrder)
        assertTrue { true }
    }

    @Given("I created an order and I'm trying to update it's status")
    fun i_created_an_order_and_im_trying_to_update_the_status_of_an_order_with_id() {
        val item = StartOrderItemRequest(
            1L,
            quantity = 1,
            toGo = true,
            observations = "sem cebola",
            price = 22
        )

        mockedOrder = Order(UUID.randomUUID(), listOf(OrderItem.fromRequest(item)))
        expectedReturn = Order.toMap(mockedOrder)
    }

    @When("I try to change it's status to {word}")
    fun i_try_to_change_its_status(status: String) {
        val body = "{ \"status\": \"${status}\" }"

        runBlocking {
            testApplication {
                environment {
                    config = ApplicationConfig("application-test.conf")
                }
                application {
                    testModule()
                }

                coEvery { orderRepository.findById(mockedOrder.id) } returns expectedReturn
                coEvery { orderRepository.update(mockedOrder.id, status) } returns Order.toMap(mockedOrder.withUpdatedStatus(OrderStatus.valueOf(status)))
                coJustRun { orderRepository.delete(mockedOrder.id) }
                coJustRun { sendPatchRequestInteract.send(mockedOrder.id, OrderStatus.COMPLETED) }
                response = client.patch("/v1/kitchen/${mockedOrder.id}") {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
            }

            assertTrue { true }
        }
    }

    @Then("The system should warn me that there's no such order")
    fun b() {
        runBlocking {
            val jsObject = JSONObject(response.bodyAsText())
            val map = jsObject.toMap()

            assertTrue { map.containsValue("No order found for id = ${mockedOrder.id}") }
        }

    }

    @Then("The system should warn me that the status {word} is invalid2")
    fun the_system_should_throw_me_an_error(status: String) {
        runBlocking {
            val jsObject = JSONObject(response.bodyAsText())
            val map = jsObject.toMap()

            assertTrue { map.containsValue("Invalid status: $status") }
        }

    }

    @Then("I should receive the order with it's status set to {word}")
    fun i_should_receive_the_order_with_it_s_status_set_to_canceled(expectedStatus: String) {
        runBlocking {
            val jsObject = JSONObject(response.bodyAsText())
            val map = jsObject.toMap()

            assertTrue { map["status"]?.equals(expectedStatus) ?: false }
        }
    }

    @Then("I should receive an error stating that the id I passed is invalid")
    fun i_should_receive_an_error_stating_that_the_id_i_passes_is_invalid() {
        runBlocking {
            assertTrue { response.bodyAsText() == "Invalid ID" }
        }
    }

    private fun Application.testModule() {
        install(Koin) {
            modules(module {
                single { orderRepository }
                single<OrderGateway> { OrderGatewayImpl(get()) }
                single {
                    UpdateOrderStatusInteract(
                        get(),
                        sendPatchRequestInteract
                    )
                }
                single { ListOrdersInteract(get()) }
                single { StartOrderInteract(get()) }
            })
        }
    }
}