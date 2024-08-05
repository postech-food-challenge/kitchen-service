package br.com.fiap.postech.configuration

import br.com.fiap.postech.application.gateways.OrderGateway
import br.com.fiap.postech.application.usecases.ListOrdersInteract
import br.com.fiap.postech.application.usecases.SendPatchRequestInteract
import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.application.usecases.UpdateOrderStatusInteract
import br.com.fiap.postech.infrastructure.aws.DynamoDbClientProvider
import br.com.fiap.postech.infrastructure.aws.IDynamoDbClientProvider
import br.com.fiap.postech.infrastructure.gateways.OrderGatewayImpl
import br.com.fiap.postech.infrastructure.persistence.OrderRepository
import br.com.fiap.postech.infrastructure.persistence.OrderRepositoryDynamoDbImpl
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

fun Application.configureKoin(config: ApplicationConfig) {
    install(Koin) {
        modules(getModules(config))
    }
}

fun getModules(config: ApplicationConfig) = module {
    single<DynamoDbClient> {
        val region = config.property("dynamodb.region").getString()
        val endpoint = config.property("dynamodb.endpoint").getString()

        DynamoDbClient.builder()
            .region(Region.of(region))
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()
    }

    single<DynamoDbClient> {
        val region = config.property("dynamodb.region").getString()
        val endpoint = config.property("dynamodb.endpoint").getString()

        if (config.property("env").getString() == "dev") {
            DynamoDbClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(
                    StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("fakeMyKeyId", "fakeSecretAccessKey")
                    )
                )
                .build()
        } else {
            DynamoDbClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()
        }
    }

    single<IDynamoDbClientProvider> {
        DynamoDbClientProvider(get())
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