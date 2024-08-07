package br.com.fiap.postech.configuration

import io.ktor.server.config.ApplicationConfig

data class AwsConfiguration(
    val account: String,
    val region: String,
    val baseUrl: String,
    val accessKey: String,
    val secretAccessKey: String,
    val paymentStatusUpdateQueueUrl: String
)

class AwsConfigurationLoader(private val config: ApplicationConfig) {
    fun load(): AwsConfiguration {
        return AwsConfiguration(
            account = config.property("aws.account").getString(),
            region = config.property("aws.region").getString(),
            baseUrl = config.property("aws.base_url").getString(),
            accessKey = config.property("aws.access_key").getString(),
            secretAccessKey = config.property("aws.secret_access_key").getString(),
            paymentStatusUpdateQueueUrl = config.property("aws.queue.order_ready_url").getString()
        )
    }
}
