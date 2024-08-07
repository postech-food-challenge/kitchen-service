package br.com.fiap.postech.infrastructure.aws.dto

import java.util.*

data class OrderReadyMessage(private val id: UUID, private val status: String)
