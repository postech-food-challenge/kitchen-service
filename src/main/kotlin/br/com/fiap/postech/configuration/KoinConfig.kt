package br.com.fiap.postech.configuration

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.SendPatchRequestInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import br.com.fiap.postech.infraestructure.aws.DynamoDBLocalClient
import br.com.fiap.postech.infraestructure.aws.DynamoDbClientProvider
import br.com.fiap.postech.infraestructure.aws.IDynamoDbClientProvider
import br.com.fiap.postech.infraestructure.gateways.OrderGatewayImpl
import br.com.fiap.postech.infraestructure.persistence.OrderRepository
import br.com.fiap.postech.infraestructure.persistence.OrderRepositoryDynamoDbImpl
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(config: ApplicationConfig) {
    install(Koin) {
        modules(getModules(config))
    }
}

fun getModules(config: ApplicationConfig) = module {
    if (config.property("env").getString() == "dev") {
        single {
            DynamoDBLocalClient()
        } bind IDynamoDbClientProvider::class
    } else {
        single {
            DynamoDbClientProvider(
                config.property("aws.access_key_id").getString(),
                config.property("aws.secret_access_key").getString(),
                config.property("aws.aws_session_token").getString(),
                config.property("aws.region").getString(),
            )
        } bind IDynamoDbClientProvider::class
    }

    single<OrderRepository> { OrderRepositoryDynamoDbImpl(get()) }
    single<OrderGateway> { OrderGatewayImpl(get()) }
    single {
        UpdateOrderStatusInteract(
            get(),
            SendPatchRequestInteract(config.property("orders-ms.complete_order_url").getString())
        )
    }
    single { ListOrdersInteract(get()) }
    single { StartOrderInteract(get()) }
}