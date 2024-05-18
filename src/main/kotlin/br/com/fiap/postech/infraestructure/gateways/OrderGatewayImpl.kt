package br.com.fiap.postech.infraestructure.gateways

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.OrderStatusComparatorProvider
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.infraestructure.persistence.OrderRepository
import java.util.*

class OrderGatewayImpl (private val orderRepository: OrderRepository) : OrderGateway {
    override suspend fun findById(id: Long): Order? {
        orderRepository.findById(id)?.let {
            return Order.fromMap(it)
        }

        return null
    }

    override suspend fun findActiveOrdersSorted(): List<Order> {
        val lisfOfDocuments =
            orderRepository.findActiveOrdersSorted() ?: Collections.emptyList()

        return lisfOfDocuments.stream().map { Order.fromMap(it) }
            .sorted(OrderStatusComparatorProvider.getComparator())
            .sorted(Comparator.comparing { it.createdAt })
            .toList()
    }

    override suspend fun findByStatus(status: String): List<Order> {
        val lisfOfDocuments =
            orderRepository.findByStatus(status) ?: Collections.emptyList()

        return lisfOfDocuments.stream().map { Order.fromMap(it) }
            .sorted(Comparator.comparing { it.createdAt })
            .toList()
    }

    override suspend fun updateOrderStatus(id: Long, newStatus: String): Order {
        val document = orderRepository.update(id, newStatus)

        return Order.fromMap(document)
    }

    override suspend fun create(id: Long, items: List<OrderItem>): Order? {
        val order = Order(
            id = id,
            items = items
        )

        orderRepository.create(order)?.let {
            return Order.fromMap(it)
        }

        return null
    }

    override suspend fun delete(id: Long) {
        orderRepository.delete(id)
    }
}