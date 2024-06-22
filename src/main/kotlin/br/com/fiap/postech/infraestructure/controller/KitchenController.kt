package br.com.fiap.postech.infraestructure.controller

import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.startOrderRoute() {
    val startOrderInteract by inject<StartOrderInteract>()

    post("/v1/kitchen/start/{id}") {
        val orderId = call.parameters["id"]

        val items = call.receive<List<StartOrderItemRequest>>()

        if (orderId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return@post
        }

        call.respond(
            HttpStatusCode.OK,
            startOrderInteract.receive(UUID.fromString(orderId), items)
        )
    }
}

fun Route.updateOrderStatusRoute() {
    val updateOrderStatusInteract by inject<UpdateOrderStatusInteract>()

    patch("/v1/kitchen/{id}") {
        var orderId: UUID?
        try {
            orderId = UUID.fromString(call.parameters["id"])
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return@patch
        }

        val bodyDto = call.receive<UpdateOrderStatusRequest>()

        call.respond(
            HttpStatusCode.OK,
            updateOrderStatusInteract.updateOrderStatus(orderId, bodyDto.status)
        )
    }
}

fun Route.findOrdersRoute() {
    val listOrdersInteract by inject<ListOrdersInteract>()

    get("/v1/kitchen") {
        val status = call.request.queryParameters["status"]

        call.respond(
            HttpStatusCode.OK,
            listOrdersInteract.listOrders(status)
        )
    }
}