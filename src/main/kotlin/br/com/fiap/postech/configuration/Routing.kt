package com.example.configuration

import com.example.application.usecases.ListOrdersInteract
import com.example.application.usecases.UpdateOrderStatusInteract
import com.example.infraestructure.controller.UpdateOrderStatusRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val updateOrderStatusInteract by inject<UpdateOrderStatusInteract>()

    val listOrdersInteract by inject<ListOrdersInteract>()


    routing {
        route("/kitchen") {
            patch("/{id}") {
                val orderId = call.parameters["id"]?.toLongOrNull();

                if (orderId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@patch
                }

                val bodyDto = call.receive<UpdateOrderStatusRequest>()

                call.respond(
                    HttpStatusCode.OK,
                    updateOrderStatusInteract.updateOrderStatus(orderId, bodyDto.status)
                );
            }

            get("") {
                val status = call.request.queryParameters["status"]

                call.respond(
                    HttpStatusCode.OK,
                    listOrdersInteract.listOrders(status)
                );
            }
        }

    }
}
