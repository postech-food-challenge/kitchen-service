package br.com.fiap.postech.application.gateways

import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem

interface OrderGateway {

    suspend fun findById(id: Long): Order?
    suspend fun findActiveOrdersSorted(): List<Order>
    suspend fun findByStatus(status: String): List<Order>
    suspend fun updateOrderStatus(id: Long, newStatus: String): Order
    suspend fun create(id: Long, items: List<OrderItem>): Order?
    suspend fun delete(id: Long)
}