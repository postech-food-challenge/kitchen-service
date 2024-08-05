package br.com.fiap.postech.application.usecases

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.domain.exceptions.NoObjectFoundException
import java.util.*

class UpdateOrderStatusInteract(
    private val orderGateway: OrderGateway,
    private val sendPatchRequestInteract: SendPatchRequestInteract
) {

    suspend fun updateOrderStatus(id: UUID, newStatus: String): Order {
        return orderGateway.findById(id)?.let {
            val status = OrderStatus.validateStatus(newStatus)

            when {
                status == OrderStatus.COMPLETED -> {
                    orderGateway.delete(id)
                    sendPatchRequestInteract.send(id, OrderStatus.COMPLETED)
                    return it.withUpdatedStatus(status)
                } else -> {
                    return orderGateway.updateOrderStatus(id, newStatus)
                }
            }
        } ?: throw NoObjectFoundException("No order found for id = $id")
    }
}