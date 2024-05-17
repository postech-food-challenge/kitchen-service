package com.example.infraestructure.persistence.entities

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime


class OrderEntity(
    id: EntityID<Long>,
    var customerCpf: String,
    var status: String,
    var createdAt: java.time.LocalDateTime,
    var items: List<OrderItemEntity>? = null
) : LongEntity(id) {
    companion object : LongEntityClass<OrderEntity>(Orders)
}

object Orders : LongIdTable() {
    val cpf = varchar("cpf", 30)
    val status = varchar("status", 30)
    val createdAt = datetime("createdAt")
}

