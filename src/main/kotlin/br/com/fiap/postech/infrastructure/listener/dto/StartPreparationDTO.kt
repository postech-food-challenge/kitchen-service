package br.com.fiap.postech.infrastructure.listener.dto

import br.com.fiap.postech.configuration.UUIDSerializer
import br.com.fiap.postech.infrastructure.controller.StartOrderItemRequest
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class StartPreparationDTO(
    @Serializable(with = UUIDSerializer::class)
    val orderId: UUID,
    val orderItems: List<StartOrderItemRequest>
) {
}
