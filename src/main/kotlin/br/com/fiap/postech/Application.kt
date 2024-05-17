package com.example

import com.example.configuration.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseConfiguration.init()
    configureSerialization()
    configureRouting()
    configureKoin()
    configureExceptionsResponse()
}
