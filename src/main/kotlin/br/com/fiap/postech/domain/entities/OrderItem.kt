package br.com.fiap.postech.domain.entities

import br.com.fiap.postech.infraestructure.controller.StartOrderItemRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

data class OrderItem(
    val name: String,
    val quantity: Int,
    val observations: String? = null,
    val toGo: Boolean
) {
    companion object {
        fun fromRequest(item: StartOrderItemRequest): OrderItem {
            return OrderItem(
                name = item.productId.toString(),
                quantity = item.quantity,
                observations = item.observations,
                toGo = item.toGo
            )
        }

        fun toMap(item: OrderItem): Map<String, AttributeValue> {
            return mapOf(
                "name" to AttributeValue.builder().s(item.name).build(),
                "quantity" to AttributeValue.builder().n(item.quantity.toString()).build(),
                "observations" to AttributeValue.builder().s(item.observations).build(),
                "toGo" to AttributeValue.builder().bool(item.toGo).build()
            )
        }

        fun fromMap(map: Map<String, AttributeValue>): OrderItem {
            val name = map["name"]?.s() ?: throw IllegalArgumentException("name is required")
            val quantity = map["quantity"]?.n()?.toInt() ?: throw IllegalArgumentException("quantity is required")
            val observations = map["observations"]?.s()
            val toGo = map["toGo"]?.bool() ?: throw IllegalArgumentException("toGo is required")
            return OrderItem(name, quantity, observations, toGo)
        }
    }
}