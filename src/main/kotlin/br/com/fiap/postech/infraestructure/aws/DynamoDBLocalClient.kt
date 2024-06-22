package br.com.fiap.postech.infraestructure.aws

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import aws.smithy.kotlin.runtime.net.url.Url

class DynamoDBLocalClient() : IDynamoDbClientProvider {
    override suspend fun getClient(): DynamoDbClient {
        return DynamoDbClient.fromEnvironment {
            credentialsProvider = StaticCredentialsProvider(
                Credentials(
                    accessKeyId = "accessKey",
                    secretAccessKey = "secretKey"
                )
            )
            endpointUrl = Url.parse("http://localhost:8000")

        }
    }
}