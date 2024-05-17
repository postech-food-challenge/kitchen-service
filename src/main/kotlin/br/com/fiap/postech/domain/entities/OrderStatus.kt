package com.example.domain.entities

import com.example.domain.exceptions.InvalidParameterException

enum class OrderStatus {
    RECEIVED,
    IN_PREPARATION,
    READY,
    COMPLETED,
    CANCELED;

    companion object {
        fun validateStatus(status: String): OrderStatus {
            return enumValues<OrderStatus>().find { it.name == status }
                ?: throw InvalidParameterException("Invalid status: $status")
        }
    }
}