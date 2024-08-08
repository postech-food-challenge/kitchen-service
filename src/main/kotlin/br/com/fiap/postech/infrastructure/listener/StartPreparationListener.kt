package br.com.fiap.postech.infrastructure.listener

import br.com.fiap.postech.application.usecases.StartOrderInteract
import br.com.fiap.postech.configuration.AwsConfiguration
import br.com.fiap.postech.infrastructure.listener.dto.StartPreparationDTO
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

class StartPreparationListener(
    private val sqsClient: SqsClient,
    private val awsConfiguration: AwsConfiguration,
    private val startOrderInteract: StartOrderInteract
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun startListening() {
        val queueUrl = awsConfiguration.startPreparationQueueUrl
        scope.launch {
            while (isActive) {
                try {
                    val receiveMessageRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(10)
                        .waitTimeSeconds(20)
                        .build()

                    val messages = sqsClient.receiveMessage(receiveMessageRequest).messages()
                    for (message in messages) {
                        println("Received message: ${message.body()}")
                        val startPreparationDTO = Json.decodeFromString<StartPreparationDTO>(message.body())

                        startOrderInteract.receive(startPreparationDTO.orderId, startPreparationDTO.orderItems)
                        val deleteMessageRequest = DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build()
                        sqsClient.deleteMessage(deleteMessageRequest)
                    }
                } catch (e: Exception) {
                    println("Error receiving messages: ${e.message}")
                }
            }
        }
    }
}