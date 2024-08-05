package br.com.fiap.postech.application.usecases

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.domain.exceptions.InvalidParameterException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class ListOrdersInteractTest {

    private val orderGateway = mockk<OrderGateway>()
    private val listOrdersInteract = ListOrdersInteract(orderGateway)

    @Test
    fun `listOrders should return active orders when status is null`() = runBlocking {
        val activeOrders = listOf(
            Order(UUID.randomUUID(), listOf(), OrderStatus.RECEIVED),
            Order(UUID.randomUUID(), listOf(), OrderStatus.IN_PREPARATION)
        )
        coEvery { orderGateway.findActiveOrdersSorted() } returns activeOrders

        val result = listOrdersInteract.listOrders(null)

        assertEquals(activeOrders, result)
        coVerify { orderGateway.findActiveOrdersSorted() }
    }

    @Test
    fun `listOrders should return active orders when status is empty`() = runBlocking {
        val activeOrders = listOf(
            Order(UUID.randomUUID(), listOf(), OrderStatus.RECEIVED),
            Order(UUID.randomUUID(), listOf(), OrderStatus.IN_PREPARATION)
        )
        coEvery { orderGateway.findActiveOrdersSorted() } returns activeOrders

        val result = listOrdersInteract.listOrders("")

        assertEquals(activeOrders, result)
        coVerify { orderGateway.findActiveOrdersSorted() }
    }

    @Test
    fun `listOrders should return orders by status when status is valid`() = runBlocking {
        val status = OrderStatus.COMPLETED.name
        val ordersByStatus = listOf(
            Order(UUID.randomUUID(), listOf(), OrderStatus.COMPLETED)
        )
        coEvery { orderGateway.findByStatus(status) } returns ordersByStatus

        val result = listOrdersInteract.listOrders(status)

        assertEquals(ordersByStatus, result)
        coVerify { orderGateway.findByStatus(status) }
    }

    @Test
    fun `listOrders should throw InvalidParameterException when status is invalid`() = runBlocking {
        val invalidStatus = "INVALID_STATUS"

        val exception = assertThrows<InvalidParameterException> {
            runBlocking {
                listOrdersInteract.listOrders(invalidStatus)
            }
        }
        assertEquals("Invalid status: $invalidStatus", exception.message)
        coVerify(exactly = 0) { orderGateway.findByStatus(any()) }
    }
}
