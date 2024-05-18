package br.com.fiap.postech.configuration

import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import br.com.fiap.postech.infraestructure.gateways.OrderGatewayImpl
import br.com.fiap.postech.infraestructure.persistence.OrderRepositoryImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
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
    single<StartOrderInteract> { StartOrderInteract(orderGateway) }
}

