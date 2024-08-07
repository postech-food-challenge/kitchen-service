package br.com.fiap.postech.application.gateways

import br.com.fiap.postech.domain.entities.OrderStatus
import java.util.*

interface MessageProducerGateway {

    suspend fun sendOrderReadyMessage(id: UUID, status: OrderStatus)

}