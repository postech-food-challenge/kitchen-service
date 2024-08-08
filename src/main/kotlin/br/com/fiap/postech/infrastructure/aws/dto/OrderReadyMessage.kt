package br.com.fiap.postech.infrastructure.aws.dto

import br.com.fiap.postech.configuration.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class OrderReadyMessage(
    @Serializable(with = UUIDSerializer::class)
    private val orderId: UUID,
    private val status: String
)
