package br.com.fiap.postech.infrastructure.controller

data class StartOrderItemRequest (
    val productId: Long,
    val quantity: Int,
    val observations: String? = null,
    val toGo: Boolean,
    val price: Int
)
