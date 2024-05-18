package br.com.fiap.postech.configuration

import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import br.com.fiap.postech.domain.entities.OrderItem
import br.com.fiap.postech.infraestructure.controller.UpdateOrderStatusRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val updateOrderStatusInteract by inject<UpdateOrderStatusInteract>()

    val listOrdersInteract by inject<ListOrdersInteract>()

    val startOrderInteract by inject<StartOrderInteract>()

    routing {
        route("/kitchen") {
            patch("/{id}") {
                val orderId = call.parameters["id"]?.toLongOrNull()

                if (orderId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@patch
                }

                val bodyDto = call.receive<UpdateOrderStatusRequest>()

                call.respond(
                    HttpStatusCode.OK,
                    updateOrderStatusInteract.updateOrderStatus(orderId, bodyDto.status)
                )
            }

            get("") {
                val status = call.request.queryParameters["status"]

                call.respond(
                    HttpStatusCode.OK,
                    listOrdersInteract.listOrders(status)
                )
            }

            post("/start/{id}") {
                val orderId = call.parameters["id"]?.toLongOrNull()

                val items = call.receive<List<OrderItem>>()

                if (orderId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@post
                }

                call.respond(
                    HttpStatusCode.OK,
                    startOrderInteract.receive(orderId, items)
                )
            }

        }

    }
}
