package br.com.fiap.postech

import br.com.fiap.postech.configuration.configureExceptionsResponse
import br.com.fiap.postech.configuration.configureKoin
import br.com.fiap.postech.configuration.configureRouting
import br.com.fiap.postech.configuration.configureSerialization
import br.com.fiap.postech.infrastructure.listener.StartPreparationListener
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureKoin(environment.config)
    configureExceptionsResponse()

    val startPreparationListener by inject<StartPreparationListener>()
    startPreparationListener.startListening()
}

fun Application.moduleTst() {
    configureSerialization()
    configureRouting()
    configureExceptionsResponse()
}