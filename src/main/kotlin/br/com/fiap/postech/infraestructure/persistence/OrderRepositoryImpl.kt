package com.example.infraestructure.persistence

import com.example.infraestructure.persistence.entities.OrderEntity
import com.example.infraestructure.persistence.entities.OrderItemEntity
import com.example.infraestructure.persistence.entities.OrderItems
import com.example.infraestructure.persistence.entities.Orders
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class OrderRepositoryImpl : OrderRepository {
    override fun findById(id: Long): OrderEntity? {

        return  transaction {
            (Orders leftJoin OrderItems)
                .select { Orders.id eq id }
                .groupBy{it[Orders.id]}
                .mapNotNull { gp ->
                    val row = gp.value.firstOrNull()
                    row?.let {
                        mapToOrderEntity(it, gp.value)
                    }
                }.singleOrNull()
        }
    }

    override fun findActiveOrdersSorted(): List<OrderEntity> {
        return  transaction {
            (Orders leftJoin OrderItems)
                .select { Orders.status notInList (listOf("COMPLETED", "CANCELED")) }
                .orderBy(statusCustomOrdering() to SortOrder.ASC)
                .groupBy{it[Orders.id]}
                .mapNotNull { gp ->
                    val row = gp.value.firstOrNull()
                    row?.let {
                        mapToOrderEntity(it, gp.value)
                    }
                }.toList()
        }

    }

    override fun findByStatus(status: String): List<OrderEntity> {
        return  transaction {
            (Orders leftJoin OrderItems)
                .select { Orders.status eq status }
                .orderBy(Orders.createdAt to SortOrder.ASC)
                .groupBy{it[Orders.id]}
                .mapNotNull { gp ->
                    val row = gp.value.firstOrNull()
                    row?.let {
                        mapToOrderEntity(it, gp.value)
                    }
                }.toList()
        }
    }


    override fun update(id: Long, newStatus: String): OrderEntity? {
        val orderEntity = findById(id)

        orderEntity ?.let {
            transaction {
                orderEntity.apply {
                    status = newStatus
                }
            }
        }
        return orderEntity
    }

    private fun mapToOrderEntity(row: ResultRow, itemEntities: List<ResultRow>): OrderEntity {
        val orderId = row[Orders.id]
        val customerCpf = row[Orders.cpf]
        val status = row[Orders.status]
        val createdAt = row[Orders.createdAt]
        val items =
            if (row[OrderItems.id] != null) {
                itemEntities.map {
                    OrderItemEntity(
                        id = it[OrderItems.id],
                        productId = it[OrderItems.productId].value,
                        orderId = it[OrderItems.orderId].value,
                        quantity = it[OrderItems.quantity],
                        observations = it[OrderItems.observations],
                        toGo = it[OrderItems.toGo]
                    )
                }
            } else {
                Collections.emptyList()
            }

        return OrderEntity(orderId, customerCpf, status, createdAt, items)
    }
}