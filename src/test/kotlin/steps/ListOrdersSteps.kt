package steps

import br.com.fiap.postech.application.gateways.MessageProducerGateway
import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import br.com.fiap.postech.configuration.AwsConfiguration
import br.com.fiap.postech.infrastructure.aws.MessageProducerGatewayImpl
import br.com.fiap.postech.infrastructure.gateways.OrderGatewayImpl
import br.com.fiap.postech.infrastructure.persistence.OrderRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.test.KoinTest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.sqs.SqsClient
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class ListOrdersSteps : KoinTest {
    private lateinit var response: HttpResponse
    private val orderRepository = mockk<OrderRepository>(relaxed = true)
    private lateinit var mockedDynamoDbResponse: List<Map<String, AttributeValue>>

    @RelaxedMockK
    private lateinit var mockSqsClient: SqsClient

    @MockK
    private lateinit var awsConfiguration: AwsConfiguration


    @Given("I have a few orders on my database")
    fun i_have_a_few_orders_on_my_database() {
        mockedDynamoDbResponse = listOf(
            mapOf(
                "id" to (AttributeValue.builder().s(UUID.randomUUID().toString()).build()),
                "items" to AttributeValue.builder().l(listOf()).build(),
                "status" to AttributeValue.builder().s("RECEIVED").build(),
                "createdAt" to AttributeValue.builder().s(LocalDateTime.now().toString()).build()
            )
        )

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
                response = client.get("/v1/kitchen?status=$status")
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
            modules(testModules)
        }
    }

    private val testModules = module {
        single { orderRepository }
        single<OrderGateway> { OrderGatewayImpl(get()) }
        single { UpdateOrderStatusInteract(get(), get()) }
        single { ListOrdersInteract(get()) }
        single { StartOrderInteract(get()) }
        single { awsConfiguration }
        single { mockSqsClient }
        single { jacksonObjectMapper() }
        single<MessageProducerGateway> { MessageProducerGatewayImpl(get(), get()) }
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        every { awsConfiguration.orderReadyQueueUrl } returns "http://localhost:4566/000000000000/test-queue"
        every { awsConfiguration.region } returns "us-west-2"
        every { awsConfiguration.accessKey } returns "test-access-key"
        every { awsConfiguration.secretAccessKey } returns "test-secret-key"
        every { awsConfiguration.account } returns "000000000000"

        mockSqsClient = mockk(relaxed = true)
        startKoin {
            modules(testModules)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}