package com.example.infraestructure.persistence.entities

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

class ProductEntity(
    id: EntityID<Long>,
    val name: String,
    val items : List<OrderItemEntity>,
    val description: String,
    val image: String,
    val price: Int,
    val category: String
) : LongEntity(id) {
    companion object : LongEntityClass<ProductEntity>(Products)
//    var products by ProductEntity referencedOn OrderItemEntity.
}

object Products : LongIdTable() {
    val name = varchar("name", 30)
    val description = varchar("description", 50)
    val image = varchar("image", 1024)
    val price = integer("price")
    val category = varchar("category", 1024)
}