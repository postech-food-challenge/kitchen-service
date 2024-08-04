package br.com.fiap.postech.domain.entities

import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Order(
    val id: UUID,
    val items: List<OrderItem>,
    var status: OrderStatus,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(id: UUID, items: List<OrderItem>) : this(id, items, OrderStatus.RECEIVED, LocalDateTime.now())

    fun withUpdatedStatus(newStatus: OrderStatus): Order {
        return this.copy(status = newStatus)
    }

    companion object {
        fun fromMap(map: Map<String, AttributeValue>): Order {
            val id = UUID.fromString(map["id"]?.s() ?: throw IllegalArgumentException("id is required"))
            val status = OrderStatus.validateStatus(map["status"]?.s() ?: throw IllegalArgumentException("status is required"))
            val createdAt = map["createdAt"]?.s()?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) } ?: throw IllegalArgumentException("createdAt is required")
            val items = map["items"]?.l()?.map { OrderItem.fromMap(it.m()) } ?: throw IllegalArgumentException("items are required")

            return Order(id, items, status, createdAt)
        }

        fun toMap(order: Order): Map<String, AttributeValue> {
            return mapOf(
                "id" to AttributeValue.builder().s(order.id.toString()).build(),
                "status" to AttributeValue.builder().s(order.status.name).build(),
                "items" to AttributeValue.builder().l(order.items.map { AttributeValue.builder().m(OrderItem.toMap(it)).build() }).build(),
                "createdAt" to AttributeValue.builder().s(order.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).build()
            )
        }
    }
}
