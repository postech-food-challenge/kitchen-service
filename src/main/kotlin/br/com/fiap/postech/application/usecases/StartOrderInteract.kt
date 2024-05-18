package br.com.fiap.postech.application.usecases

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem

class StartOrderInteract(private val orderGateway: OrderGateway) {

    suspend fun receive(id: Long, items: List<OrderItem>): Order {
        orderGateway.create(id, items)
            ?.let { return it }
            ?: throw NullPointerException(": F")
    }
}