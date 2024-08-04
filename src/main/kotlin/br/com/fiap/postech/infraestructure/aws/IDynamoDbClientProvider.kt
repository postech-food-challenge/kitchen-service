package br.com.fiap.postech.infraestructure.aws

import software.amazon.awssdk.services.dynamodb.DynamoDbClient

interface IDynamoDbClientProvider {
    suspend fun getClient(): DynamoDbClient
}
