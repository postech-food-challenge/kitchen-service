package steps

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.SendPatchRequestInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import br.com.fiap.postech.infraestructure.gateways.OrderGatewayImpl
import br.com.fiap.postech.infraestructure.persistence.OrderRepository
import br.com.fiap.postech.infraestructure.persistence.OrderRepositoryDynamoDbImpl
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.test.KoinTest
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertTrue

class ListOrdersSteps: KoinTest {
    private lateinit var response: HttpResponse
    private val orderRepository = mockk<OrderRepository>(relaxed = false)
    private lateinit var mockedDynamoDbResponse: List<Map<String, AttributeValue>>

    @Given("I have a few orders on my database")
    fun i_have_a_few_orders_on_my_database() {
        mockedDynamoDbResponse = listOf(mapOf(
            "id" to (AttributeValue.S(UUID.randomUUID().toString())),
            "items" to AttributeValue.L(listOf()),
            "status" to AttributeValue.S("RECEIVED"),
            "createdAt" to AttributeValue.S(LocalDateTime.now().toString())
        ))

        assertTrue { mockedDynamoDbResponse.isNotEmpty() }
    }

    @When("I try to find orders on status = {word}")
    fun i_try_to_find_orders_on_status(status: String) {
        runBlocking {
            testApplication {
                environment {
                    config = ApplicationConfig("application-test.conf")
                }
                application {
                    testModule()
                }
                coEvery { orderRepository.findByStatus(status) } returns mockedDynamoDbResponse
                response = client.get("/v1/kitchen?status=${status}")
            }

            assertTrue { true }
        }
    }

    @Then("The system should warn me that the status {word} is invalid")
    fun the_system_should_throw_me_an_error2(status: String) {
        runBlocking {
            val body = response.bodyAsText()

            assertTrue { body.contains("Invalid status: $status") }
        }
    }

    @When("I try to find orders without status filter")
    fun i_try_to_find_orders_without_status_filter() {
        runBlocking {
            testApplication {
                environment {
                    config = ApplicationConfig("application-test.conf")
                }
                application {
                    testModule()
                }
                coEvery { orderRepository.findActiveOrdersSorted() } returns mockedDynamoDbResponse
                response = client.get("/v1/kitchen")
            }
            assertTrue { true }
        }
    }

    @Then("The system should return all orders whose status aren't on the list")
    fun the_system_should_return_all_orders_whose_status_aren_t_on_the_list(statusInativos: List<String>) {
        runBlocking {
            val jsArray = JSONArray(response.bodyAsText())

            val count = (0 until jsArray.length())
                .map { jsArray.getJSONObject(it) }
                .count { it.optString("status") in statusInativos }

            assertTrue { count == 0 }
        }
    }

    @Then("The system should return all orders on {word} status")
    fun the_system_should_return_all_orders_on_status(status: String) {
        runBlocking {
            val jsArray = JSONArray(response.bodyAsText())

            val count = (0 until jsArray.length())
                .map { jsArray.getJSONObject(it) }
                .count { it.optString("status") == status }

            assertTrue { jsArray.length() == count }
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

    @Before
    fun setUp() {
        startKoin {
            modules(module {
                single { OrderRepositoryDynamoDbImpl }
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

    @After
    fun tearDown() {
        stopKoin()
    }
}