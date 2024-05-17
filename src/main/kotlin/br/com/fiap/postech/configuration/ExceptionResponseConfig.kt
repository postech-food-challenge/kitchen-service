package com.example.configuration

import com.example.domain.exceptions.ExceptionResponse
import com.example.domain.exceptions.InvalidParameterException
import com.example.domain.exceptions.NoObjectFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptionsResponse() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is NoObjectFoundException ->
                    call.respond(
                        HttpStatusCode.OK,
                        ExceptionResponse("${throwable.message}", HttpStatusCode.OK.value)
                    )
                is InvalidParameterException ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ExceptionResponse("${throwable.message}", HttpStatusCode.BadRequest.value)
                    )
            }

        }
    }
}