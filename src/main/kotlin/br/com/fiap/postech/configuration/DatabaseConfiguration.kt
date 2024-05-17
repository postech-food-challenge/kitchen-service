package com.example.configuration

import com.example.infraestructure.persistence.entities.OrderItems
import com.example.infraestructure.persistence.entities.Orders
import com.example.infraestructure.persistence.entities.Products
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object DatabaseConfiguration {
    fun init() {
        val database = Database.connect(
            url = "jdbc:postgresql://localhost:5432/kitchen-db",
            driver = "org.postgresql.Driver",
            user = "food-challenge",
            password = "root"
        )

        transaction (database) {
            org.jetbrains.exposed.sql.SchemaUtils.create(Orders)
            org.jetbrains.exposed.sql.SchemaUtils.create(Products)
            org.jetbrains.exposed.sql.SchemaUtils.create(OrderItems)

            ordersMigration()
            productsMigration()
            orderItemsMigration()
        }
    }
}

private fun ordersMigration() {
    Orders.insert {
        it[Orders.cpf] = "02789033200"
        it[Orders.status] = "RECEIVED"
        it[Orders.createdAt] = LocalDateTime.now()
    }
    Orders.insert {
        it[Orders.cpf] = "75382731071"
        it[Orders.status] = "IN_PREPARATION"
        it[Orders.createdAt] = LocalDateTime.now()
    }
    Orders.insert {
        it[Orders.cpf] = "68932877041"
        it[Orders.status] = "RECEIVED"
        it[Orders.createdAt] = LocalDateTime.now()
    }
    Orders.insert {
        it[Orders.cpf] = "68205445028"
        it[Orders.status] = "CANCELED"
        it[Orders.createdAt] = LocalDateTime.now()
    }
}

private fun productsMigration() {
    Products.insert {
        it[Products.name] = "X-BACON"
        it[Products.price] = 22
        it[Products.image] = "NOHAVE"
        it[Products.category] = "SANDUICHE"
        it[Products.description] = "Sanduiche com bacon"
    }
    Products.insert {
        it[Products.name] = "COCA-COLA"
        it[Products.price] = 8
        it[Products.image] = "NOHAVE"
        it[Products.category] = "BEBIDA"
        it[Products.description] = "Uma bebida maravilhosa a base de cola"
    }
}

private fun orderItemsMigration() {
    OrderItems.insert {
        it[OrderItems.productId] = 1
        it[OrderItems.orderId] = 1
        it[OrderItems.toGo] = true
        it[OrderItems.quantity] = 3
        it[OrderItems.observations] = "None"
    }
    OrderItems.insert {
        it[OrderItems.productId] = 2
        it[OrderItems.orderId] = 1
        it[OrderItems.toGo] = true
        it[OrderItems.quantity] = 2
        it[OrderItems.observations] = "Cuidado para não atrasar"
    }
    OrderItems.insert {
        it[OrderItems.productId] = 1
        it[OrderItems.orderId] = 2
        it[OrderItems.toGo] = false
        it[OrderItems.quantity] = 4
        it[OrderItems.observations] = "None"
    }
}