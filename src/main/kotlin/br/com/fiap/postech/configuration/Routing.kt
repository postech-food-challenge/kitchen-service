package br.com.fiap.postech.configuration

import br.com.fiap.postech.infraestructure.controller.findOrdersRoute
import br.com.fiap.postech.infraestructure.controller.startOrderRoute
import br.com.fiap.postech.infraestructure.controller.updateOrderStatusRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        startOrderRoute()
        findOrdersRoute()
        updateOrderStatusRoute()
    }
}
