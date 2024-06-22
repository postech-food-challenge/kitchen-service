package br.com.fiap.postech.infraestructure.aws

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient

interface IDynamoDbClientProvider {
    suspend fun getClient(): DynamoDbClient;

}