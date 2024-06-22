package br.com.fiap.postech.application.usecases

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.infraestructure.controller.StartOrderItemRequest
import java.util.*

class StartOrderInteract(private val orderGateway: OrderGateway) {

    suspend fun receive(id: UUID, items: List<StartOrderItemRequest>): Order {
        orderGateway.create(id, items.map { OrderItem.fromRequest(it) })
            ?.let { return it }
            ?: throw NullPointerException(": F")
    }
}