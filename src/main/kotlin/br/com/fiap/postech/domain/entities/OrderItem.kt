package br.com.fiap.postech.domain.entities

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue

data class OrderItem(
    val name: String,
    val quantity: Int,
    val observations: String? = null,
    val toGo: Boolean
) {
    companion object {
        fun fromMap(map: Map<String, AttributeValue>): OrderItem {
            return OrderItem(
                name = map["name"]?.asS() ?: "",
                quantity = map["quantity"]?.asN()?.toInt() ?: 0,
                observations = map["observations"]?.asS(),
                toGo = map["toGo"]?.asBool() ?: false
            )
        }

        fun toMap(item: OrderItem): Map<String, AttributeValue> {
            return mapOf(
                "name" to AttributeValue.S(item.name),
                "quantity" to AttributeValue.N(item.quantity.toString()),
                "observations" to (item.observations?.let { AttributeValue.S(it) } ?: AttributeValue.Null(true)),
                "toGo" to AttributeValue.Bool(item.toGo)
            )
        }
    }
}