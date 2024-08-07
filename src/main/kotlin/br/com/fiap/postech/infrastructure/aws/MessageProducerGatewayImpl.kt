package br.com.fiap.postech.infrastructure.aws

import br.com.fiap.postech.application.gateways.MessageProducerGateway
import br.com.fiap.postech.configuration.AwsConfiguration
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.infrastructure.aws.dto.OrderReadyMessage
import com.fasterxml.jackson.databind.ObjectMapper
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import java.util.*

class MessageProducerGatewayImpl(
    private val sqsClient: SqsClient,
    private val awsConfiguration: AwsConfiguration,
    private val objectMapper: ObjectMapper
) : MessageProducerGateway {
    override suspend fun sendOrderReadyMessage(id: UUID, status: OrderStatus) {
        val message = OrderReadyMessage(id, status.name)
        val messageBody = objectMapper.writeValueAsString(message)

        val messageRequest = SendMessageRequest.builder()
            .queueUrl(awsConfiguration.paymentStatusUpdateQueueUrl)
            .messageBody(messageBody)
            .build()

        sqsClient.sendMessage(messageRequest)
    }
}