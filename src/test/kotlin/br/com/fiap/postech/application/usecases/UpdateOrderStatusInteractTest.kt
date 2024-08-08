package br.com.fiap.postech.application.usecases

import br.com.fiap.postech.application.gateways.MessageProducerGateway
import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.domain.exceptions.InvalidParameterException
import br.com.fiap.postech.domain.exceptions.NoObjectFoundException
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class UpdateOrderStatusInteractTest {

    private lateinit var orderGateway: OrderGateway
    private lateinit var updateOrderStatusInteract: UpdateOrderStatusInteract
    private lateinit var messageProducerGateway: MessageProducerGateway

    @BeforeEach
    fun setUp() {
        orderGateway = mockk()
        messageProducerGateway = mockk()
        updateOrderStatusInteract = UpdateOrderStatusInteract(orderGateway, messageProducerGateway)
    }

    @Test
    fun `should update order status when order exists`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val newStatus = OrderStatus.IN_PREPARATION.name
            val existingOrder = Order(orderId, createOrderItems(), OrderStatus.RECEIVED)
            val updatedOrder = existingOrder.withUpdatedStatus(OrderStatus.IN_PREPARATION)

            coEvery { orderGateway.findById(orderId) } returns existingOrder
            coEvery { orderGateway.updateOrderStatus(orderId, newStatus) } returns updatedOrder

            val result = updateOrderStatusInteract.updateOrderStatus(orderId, newStatus)

            assertNotNull(result)
            assertEquals(OrderStatus.IN_PREPARATION, result.status)
            coVerify { orderGateway.updateOrderStatus(orderId, newStatus) }
        }
    }

    @Test
    fun `should throw NoObjectFoundException when order does not exist`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val newStatus = OrderStatus.IN_PREPARATION.name

            coEvery { orderGateway.findById(orderId) } returns null

            assertThrows<NoObjectFoundException> {
                runBlocking {
                    updateOrderStatusInteract.updateOrderStatus(orderId, newStatus)
                }
            }
        }
    }

    @Test
    fun `should send order ready message when status is COMPLETED`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val newStatus = OrderStatus.COMPLETED.name
            val existingOrder = Order(orderId, createOrderItems(), OrderStatus.IN_PREPARATION)
            val updatedOrder = existingOrder.withUpdatedStatus(OrderStatus.COMPLETED)

            coEvery { orderGateway.findById(orderId) } returns existingOrder
            coEvery { orderGateway.updateOrderStatus(orderId, newStatus) } returns updatedOrder
            coJustRun { messageProducerGateway.sendOrderReadyMessage(orderId, OrderStatus.COMPLETED) }

            val result = updateOrderStatusInteract.updateOrderStatus(orderId, newStatus)

            assertNotNull(result)
            assertEquals(OrderStatus.COMPLETED, result.status)
            coVerify { orderGateway.updateOrderStatus(orderId, newStatus) }
            coVerify { messageProducerGateway.sendOrderReadyMessage(orderId, OrderStatus.COMPLETED) }
        }
    }

    @Test
    fun `should not send order ready message when status is not COMPLETED`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val newStatus = OrderStatus.IN_PREPARATION.name
            val existingOrder = Order(orderId, createOrderItems(), OrderStatus.RECEIVED)
            val updatedOrder = existingOrder.withUpdatedStatus(OrderStatus.IN_PREPARATION)

            coEvery { orderGateway.findById(orderId) } returns existingOrder
            coEvery { orderGateway.updateOrderStatus(orderId, newStatus) } returns updatedOrder

            val result = updateOrderStatusInteract.updateOrderStatus(orderId, newStatus)

            assertNotNull(result)
            assertEquals(OrderStatus.IN_PREPARATION, result.status)
            coVerify { orderGateway.updateOrderStatus(orderId, newStatus) }
            coVerify(exactly = 0) { messageProducerGateway.sendOrderReadyMessage(any(), any()) }
        }
    }

    @Test
    fun `should throw InvalidParameterException for invalid order status`() {
        runBlocking {
            val orderId = UUID.randomUUID()
            val invalidStatus = "INVALID_STATUS"
            val existingOrder = Order(orderId, createOrderItems(), OrderStatus.RECEIVED)

            coEvery { orderGateway.findById(orderId) } returns existingOrder

            assertThrows<InvalidParameterException> {
                runBlocking {
                    updateOrderStatusInteract.updateOrderStatus(orderId, invalidStatus)
                }
            }
        }
    }

    private fun createOrderItems(): List<OrderItem> {
        return listOf(
            OrderItem(name = "Item 1", quantity = 1, observations = "Observation 1", toGo = false),
            OrderItem(name = "Item 2", quantity = 2, observations = "Observation 2", toGo = true)
        )
    }
}

