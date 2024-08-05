package steps

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.SendPatchRequestInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.infrastructure.controller.StartOrderItemRequest
import br.com.fiap.postech.infrastructure.gateways.OrderGatewayImpl
import br.com.fiap.postech.infrastructure.persistence.OrderRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.test.KoinTest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertTrue

class StartOrderSteps : KoinTest {

    private lateinit var response: HttpResponse
    private lateinit var id: UUID
    private lateinit var mockedDynamoDbOrder: Map<String, AttributeValue>
    private val orderRepository = mockk<OrderRepository>(relaxed = true) // Set relaxed to true for easier mocking

    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
    }

    @Given("The order has been paid")
    fun the_order_has_been_paid() {
        id = UUID.randomUUID()
        assertTrue { true }
    }

    @When("I receive the order on my system")
    fun i_receive_the_order_on_my_system() {
        val item = StartOrderItemRequest(
            1L,
            quantity = 1,
            toGo = true,
            observations = "sem cebola",
            price = 10
        )

        val order = Order(
            id,
            listOf(OrderItem.fromRequest(item)),
            status = OrderStatus.RECEIVED, // Ensure status is set
            createdAt = LocalDateTime.now()
        )

        mockedDynamoDbOrder = mapOf(
            "id" to AttributeValue.builder().s(id.toString()).build(),
            "items" to AttributeValue.builder().l(order.items.map { it.toAttributeValue() }).build(),
            "status" to AttributeValue.builder().s("RECEIVED").build(),
            "createdAt" to AttributeValue.builder().s(order.createdAt.toString()).build()
        )

        runBlocking {
            testApplication {
                environment {
                    config = ApplicationConfig("application-test.conf")
                }
                application {
                    testModule()
                }

                // Ensure the mock is configured correctly
                coEvery { orderRepository.create(any()) } returns mockedDynamoDbOrder

                response = client.post("/v1/kitchen/start/${id}") {
                    contentType(ContentType.Application.Json)
                    setBody(objectMapper.writeValueAsString(listOf(item)))
                }
            }

            assertTrue { true }
        }
    }

    @Then("The order should be registered and it's status should be RECEIVED")
    fun the_order_should_be_registered_and_it_s_status_should_be_received() {
        runBlocking {
            val jsObject = JSONObject(response.bodyAsText())
            val map = jsObject.toMap()

            assertTrue { map["status"]?.equals(OrderStatus.RECEIVED.name) ?: false }
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
                        SendPatchRequestInteract("http://mocked.url")
                    )
                }
                single { ListOrdersInteract(get()) }
                single { StartOrderInteract(get()) }
            })
        }
    }
}

// Extension function to convert OrderItem to AttributeValue for DynamoDB mock
private fun OrderItem.toAttributeValue(): AttributeValue {
    return AttributeValue.builder().m(
        mapOf(
            "name" to AttributeValue.builder().s(this.name).build(),
            "quantity" to AttributeValue.builder().n(this.quantity.toString()).build(),
            "observations" to AttributeValue.builder().s(this.observations).build(),
            "toGo" to AttributeValue.builder().bool(this.toGo).build()
        )
    ).build()
}
