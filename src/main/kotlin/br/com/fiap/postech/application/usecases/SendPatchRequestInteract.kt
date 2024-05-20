package br.com.fiap.postech.application.usecases

import br.com.fiap.postech.domain.entities.OrderStatus
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import java.util.UUID

class SendPatchRequestInteract(private val url: String) {
    suspend fun send(id: UUID, status: OrderStatus) {
        val client = HttpClient(CIO)

// TODO: UNCOMMENT THIS CODE

//        val response = client.patch("$url/${id}") {
//            contentType(ContentType.Application.Json)
//            setBody(mapOf("status" to status.name))
//        }

        client.close()
    }
}