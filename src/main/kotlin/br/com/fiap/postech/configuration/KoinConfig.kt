package com.example.configuration

import com.example.application.usecases.ListOrdersInteract
import com.example.application.usecases.UpdateOrderStatusInteract
import com.example.infraestructure.gateways.OrderGatewayImpl
import com.example.infraestructure.persistence.OrderRepositoryImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(modules)
    }
}

val modules = module {
    val orderRepository = OrderRepositoryImpl()
    val orderGateway = OrderGatewayImpl(orderRepository)
    single<OrderGatewayImpl> { orderGateway }
    single<UpdateOrderStatusInteract> { UpdateOrderStatusInteract(orderGateway) }
    single<ListOrdersInteract> { ListOrdersInteract(orderGateway) }
}

