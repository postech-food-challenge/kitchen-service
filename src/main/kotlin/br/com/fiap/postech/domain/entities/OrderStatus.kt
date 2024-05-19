package br.com.fiap.postech.domain.entities

import br.com.fiap.postech.domain.exceptions.InvalidParameterException

enum class OrderStatus {
    RECEIVED,
    IN_PREPARATION,
    COMPLETED,
    CANCELED;

    companion object {
        fun validateStatus(status: String): OrderStatus {
            return enumValues<OrderStatus>().find { it.name == status }
                ?: throw InvalidParameterException("Invalid status: $status")
        }
    }
}