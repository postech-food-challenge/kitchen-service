package br.com.fiap.postech.application.usecases

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderStatus

class ListOrdersInteract(private val orderGateway: OrderGateway) {

    suspend fun listOrders(status: String?): List<Order> {
        return when {
            status.isNullOrEmpty() ->
                orderGateway.findActiveOrdersSorted()
            else -> OrderStatus.validateStatus(status)
                .let { orderGateway.findByStatus(status) }
        }
    }
}