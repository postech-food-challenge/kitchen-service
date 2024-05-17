package com.example.infraestructure.persistence

import com.example.infraestructure.persistence.entities.OrderEntity

interface OrderRepository {
    fun findById(id: Long): OrderEntity?
    fun findActiveOrdersSorted(): List<OrderEntity>
    fun findByStatus(status: String): List<OrderEntity>
    fun update(id: Long, newStatus: String): OrderEntity?
}