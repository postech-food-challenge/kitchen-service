package br.com.fiap.postech.configuration

import io.ktor.server.config.*

data class AwsConfiguration(
    val account: String,
    val region: String,
    val baseUrl: String,
    val accessKey: String,
    val secretAccessKey: String,
    val orderReadyQueueUrl: String,
    val startPreparationQueueUrl: String
)

class AwsConfigurationLoader(private val config: ApplicationConfig) {
    fun load(): AwsConfiguration {
        return AwsConfiguration(
            account = config.property("aws.account").getString(),
            region = config.property("aws.region").getString(),
            baseUrl = config.property("aws.base_url").getString(),
            accessKey = config.property("aws.access_key").getString(),
            secretAccessKey = config.property("aws.secret_access_key").getString(),
            orderReadyQueueUrl = config.property("aws.queue.order_ready_url").getString(),
            startPreparationQueueUrl = config.property("aws.queue.start_preparation_queue_url").getString()
        )
    }
}
