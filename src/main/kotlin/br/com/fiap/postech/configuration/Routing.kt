package br.com.fiap.postech.configuration

import br.com.fiap.postech.infrastructure.controller.findOrdersRoute
import br.com.fiap.postech.infrastructure.controller.startOrderRoute
import br.com.fiap.postech.infrastructure.controller.updateOrderStatusRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        startOrderRoute()
        findOrdersRoute()
        updateOrderStatusRoute()
    }
}
