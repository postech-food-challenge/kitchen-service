package com.example.domain.entities

import com.example.infraestructure.persistence.entities.OrderEntity
import com.example.infraestructure.persistence.entities.OrderItemEntity
import java.time.LocalDateTime
import java.util.Collections
import java.util.stream.Collectors

data class Order(
    val id: Long? = null,
    val customerCpf: String? = null,
    val items: List<OrderItem>,
    var status: OrderStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val paymentValidated: Boolean? = null,
    val price: Int? = null,
    val qrData: String? = null,
    val inStoreOrderId: String? = null
) {
    companion object {
        fun fromEntity(entity: OrderEntity): Order {
            return Order(
                entity.id.value,
                entity.customerCpf,
                entity.items?.map { OrderItem.fromEntity(it) } ?: Collections.emptyList(),
                OrderStatus.valueOf(entity.status),
                entity.createdAt
            )
        }
    }
}