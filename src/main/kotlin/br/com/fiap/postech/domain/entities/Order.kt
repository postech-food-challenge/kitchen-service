package br.com.fiap.postech.domain.entities

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class Order(
    val id: Long,
    val items: List<OrderItem>,
    var status: OrderStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {

    constructor(id: Long, items: List<OrderItem>): this(id, items, OrderStatus.RECEIVED, LocalDateTime.now()) { }

    fun withUpdatedStatus(newStatus: OrderStatus): Order {
        return this.copy(status = newStatus)
    }

    companion object {
        fun fromMap(map: Map<String, AttributeValue>): Order {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

            val id = map["id"]?.asN()?.toLong()
            val items = map["items"]?.asL()?.map {
                item -> OrderItem.fromMap(item.asM())
            } ?: Collections.emptyList()
            val status = OrderStatus.valueOf(map["status"]?.asS() ?: "PENDING")
            val createdAt = LocalDateTime.parse(map["createdAt"]?.asS(), formatter)

            return Order(
                id = id!!,
                items = items,
                status = status,
                createdAt = createdAt
            )
        }

        fun toMap(order: Order): Map<String, AttributeValue> {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            return mapOf(
                "id" to (order.id.let { AttributeValue.N(it.toString()) }),
                "items" to AttributeValue.L(order.items.map { AttributeValue.M (OrderItem.toMap(it))}),
                "status" to AttributeValue.S(order.status.name),
                "createdAt" to AttributeValue.S(order.createdAt.format(formatter))
            )
        }

    }
}
