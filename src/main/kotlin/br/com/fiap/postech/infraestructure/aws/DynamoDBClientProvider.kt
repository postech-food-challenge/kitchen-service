package br.com.fiap.postech.infraestructure.aws

import software.amazon.awssdk.services.dynamodb.DynamoDbClient

class DynamoDbClientProvider(private val dynamoDbClient: DynamoDbClient) : IDynamoDbClientProvider {
    override suspend fun getClient(): DynamoDbClient {
        return dynamoDbClient
    }
}
