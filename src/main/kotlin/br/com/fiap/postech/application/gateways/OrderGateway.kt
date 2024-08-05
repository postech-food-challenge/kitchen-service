package br.com.fiap.postech.application.gateways

import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import java.util.*

interface OrderGateway {

    suspend fun findById(id: UUID): Order?
    suspend fun findActiveOrdersSorted(): List<Order>
    suspend fun findByStatus(status: String): List<Order>
    suspend fun updateOrderStatus(id: UUID, newStatus: String): Order
    suspend fun create(id: UUID, items: List<OrderItem>): Order?
    suspend fun delete(id: UUID)
}