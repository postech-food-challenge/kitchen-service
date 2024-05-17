package com.example.application.usecases

import com.example.application.gateways.OrderGateway
import com.example.domain.entities.Order
import com.example.domain.entities.OrderStatus
import com.example.domain.exceptions.NoObjectFoundException

class UpdateOrderStatusInteract(private val orderGateway: OrderGateway) {

    fun updateOrderStatus(id: Long, newStatus: String): Order {
        return orderGateway.findById(id)?.let {
            OrderStatus.validateStatus(newStatus)
            orderGateway.updateOrderStatus(id, newStatus)
        }?: throw NoObjectFoundException("No order found for id = $id")
    }
}