package br.com.fiap.postech.application.usecases

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.domain.exceptions.NoObjectFoundException
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*

class UpdateOrderStatusInteract(private val orderGateway: OrderGateway) {

    suspend fun updateOrderStatus(id: Long, newStatus: String): Order {
        return orderGateway.findById(id)?.let {
            val status = OrderStatus.validateStatus(newStatus)

            when {
                status == OrderStatus.READY -> {
                    orderGateway.delete(id)
                    sendPatchRequest(id) // TODO: NÃO ESTÁ RESPEITANDO CLEAN ARCH. REFATORAR
                    return it.withUpdatedStatus(status)
                }

                else -> {
                    return orderGateway.updateOrderStatus(id, newStatus)
                }
            }
        } ?: throw NoObjectFoundException("No order found for id = $id")
    }

    suspend fun sendPatchRequest(id: Long) {
        val c = HttpClient(CIO)
        c.patch("http://localhost:8080/order/end/$id") { // TODO: ARRUMAR ISSO.
        }
    }
}