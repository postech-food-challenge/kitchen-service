package br.com.fiap.postech.application.usecases

import br.com.fiap.postech.application.gateways.MessageProducerGateway
import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.domain.exceptions.NoObjectFoundException
import java.util.*

class UpdateOrderStatusInteract(
    private val orderGateway: OrderGateway,
    private val messageProducerGateway: MessageProducerGateway
) {

    suspend fun updateOrderStatus(id: UUID, newStatus: String): Order {
        orderGateway.findById(id) ?: throw NoObjectFoundException("No order found for id = $id")
        val status = OrderStatus.validateStatus(newStatus)

        if (status == OrderStatus.COMPLETED) {
            messageProducerGateway.sendOrderReadyMessage(id, OrderStatus.COMPLETED)
        }

        return orderGateway.updateOrderStatus(id, status.name)
    }
}