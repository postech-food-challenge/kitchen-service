package com.example.domain.entities

import com.example.infraestructure.persistence.entities.OrderItemEntity

data class OrderItem(
    val productId: Long,
    val quantity: Int,
    val observations: String? = null,
    val toGo: Boolean
) {
    companion object {
        fun fromEntity(entity: OrderItemEntity): OrderItem {
            return OrderItem(
                entity.productId,
                entity.quantity,
                entity.observations,
                entity.toGo
            )
        }
    }
}