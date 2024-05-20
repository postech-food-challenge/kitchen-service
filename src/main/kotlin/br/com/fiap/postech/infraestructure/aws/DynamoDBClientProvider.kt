package br.com.fiap.postech.infraestructure.aws

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials

class DynamoDbClientProvider(
    private val accessKey: String,
    private val secretKey: String,
    private val accessToken: String,
    private val awsRegion: String
    ) {

    suspend fun getClient(): DynamoDbClient {
        return DynamoDbClient.fromEnvironment {
            credentialsProvider = StaticCredentialsProvider(
                Credentials(
                    accessKeyId = accessKey,
                    secretAccessKey = secretKey,
                    sessionToken = accessToken
                )
            )
            region = awsRegion
        }
    }
}