package com.example.application.gateways

import com.example.domain.entities.Order
import com.example.infraestructure.persistence.entities.OrderEntity

interface OrderGateway {

    fun findById(id: Long): Order?
    fun findActiveOrdersSorted(): List<OrderEntity>
    fun findByStatus(status: String): List<OrderEntity>
    fun updateOrderStatus(id: Long, newStatus: String): Order?

}