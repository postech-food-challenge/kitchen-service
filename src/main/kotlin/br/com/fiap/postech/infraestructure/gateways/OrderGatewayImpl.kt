package com.example.infraestructure.gateways

import com.example.application.gateways.OrderGateway
import com.example.domain.entities.Order
import com.example.infraestructure.persistence.OrderRepository
import com.example.infraestructure.persistence.entities.OrderEntity

class OrderGatewayImpl (private val orderRepository: OrderRepository) : OrderGateway {
    override fun findById(id: Long): Order? {
        val orderEntity = orderRepository.findById(id)

        return orderEntity?.let {
           Order.fromEntity(it)
        }
    }

    override fun findActiveOrdersSorted(): List<OrderEntity> {
        return orderRepository.findActiveOrdersSorted()
    }

    override fun findByStatus(status: String): List<OrderEntity> {
        return orderRepository.findByStatus(status)
    }

    override fun updateOrderStatus(id: Long, newStatus: String): Order? {
        return orderRepository.update(id, newStatus)?.let {
            Order.fromEntity(it)
        }
    }
}