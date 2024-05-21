package br.com.fiap.postech.infraestructure.controller

class StartOrderItemRequest (
    val productId: Long,
    val quantity: Int,
    val observations: String? = null,
    val toGo: Boolean,
    val price: Int
)

