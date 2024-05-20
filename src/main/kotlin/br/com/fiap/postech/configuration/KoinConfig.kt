package br.com.fiap.postech.configuration

import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.SendPatchRequestInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import br.com.fiap.postech.infraestructure.aws.DynamoDbClientProvider
import br.com.fiap.postech.infraestructure.gateways.OrderGatewayImpl
import br.com.fiap.postech.infraestructure.persistence.OrderRepositoryDynamoDbImpl
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(config: ApplicationConfig) {
    install(Koin) {
        modules(getModules(config))
    }
}

fun getModules(config: ApplicationConfig): Module {
    return module {
        val dynamoClientProvider = DynamoDbClientProvider(
            config.property("aws.access_key_id").getString(),
            config.property("aws.secret_access_key").getString(),
            config.property("aws.aws_session_token").getString(),
            config.property("aws.region").getString()
        )
        val orderRepository = OrderRepositoryDynamoDbImpl(dynamoClientProvider)
        val orderGateway = OrderGatewayImpl(orderRepository)
        single<OrderGatewayImpl> { orderGateway }
        single<UpdateOrderStatusInteract> { UpdateOrderStatusInteract(orderGateway, SendPatchRequestInteract(config.property("orders-ms.complete_order_url").getString())) }
        single<ListOrdersInteract> { ListOrdersInteract(orderGateway) }
        single<StartOrderInteract> { StartOrderInteract(orderGateway) }
    }
}