package br.com.fiap.postech.infrastructure.aws

import software.amazon.awssdk.services.dynamodb.DynamoDbClient

interface IDynamoDbClientProvider {
    suspend fun getClient(): DynamoDbClient
}
