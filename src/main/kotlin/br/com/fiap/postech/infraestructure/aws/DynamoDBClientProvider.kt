package br.com.fiap.postech.infraestructure.aws

import aws.sdk.kotlin.runtime.auth.credentials.ProfileCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient

object DynamoDBClientProvider {
    suspend fun getClient(): DynamoDbClient {
        return DynamoDbClient.fromEnvironment {
            credentialsProvider = ProfileCredentialsProvider(
                profileName = "default",
                region = "us-east-1"
            )
        }
    }
}