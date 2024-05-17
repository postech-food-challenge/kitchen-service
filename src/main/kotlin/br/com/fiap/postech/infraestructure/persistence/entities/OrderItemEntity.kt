package com.example.infraestructure.persistence.entities

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

class OrderItemEntity(
    id: EntityID<Long>,
    var productId: Long,
    var orderId: Long,
    var quantity: Int,
    var observations: String,
    var toGo: Boolean
) : LongEntity(id) {
    companion object : LongEntityClass<OrderItemEntity>(OrderItems)
}
//

object OrderItems : LongIdTable() {
    val productId = reference("productId", Products.id)
    val orderId = reference("orderId", Orders.id)
    val quantity = integer("quantity")
    val observations = varchar("observations", 500)
    val toGo = bool("toGo")
}