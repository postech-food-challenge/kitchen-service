package com.example.application.usecases

import com.example.application.gateways.OrderGateway
import com.example.domain.entities.Order
import com.example.domain.entities.OrderStatus
import java.util.Collections
import java.util.stream.Collectors

class ListOrdersInteract(private val orderGateway: OrderGateway) {

    fun listOrders(status: String?): List<Order> {
        val orders = when {
            status.isNullOrEmpty() -> orderGateway.findActiveOrdersSorted()
            else -> OrderStatus.validateStatus(status).let { orderGateway.findByStatus(status) }
        }

        return orders.stream().map { Order.fromEntity(it) }.collect(Collectors.toList())
    }
}