package br.com.fiap.postech.infraestructure.aws

import aws.sdk.kotlin.runtime.auth.credentials.ProfileCredentialsProvider
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import io.ktor.server.config.*

object DynamoDBClientProvider {

    private var AWS_KEY : String? = null
    private var AWS_SECRET : String? = null
    private var AWS_SESSION : String? = null
    private var REGION : String? = null


    //    access_key_id = "ASIARZFSFOMUKWSV4Y7A"
//    secret_access_key = "rSG26P4XeKPBiNA+Or+Z+deiJNWnPKz4jYQjsCMS"
//    region = "us-east-1"
//
    fun init(config: ApplicationConfig) {
        AWS_KEY = config.property("aws.access_key_id").getString()
        AWS_SECRET = config.property("aws.secret_access_key").getString()
        AWS_SESSION = config.property("aws.aws_session_token").getString()
        REGION = config.property("aws.region").getString()
    }

    suspend fun getClient(): DynamoDbClient {
//        return DynamoDbClient.fromEnvironment {
//            credentialsProvider = ProfileCredentialsProvider(
//                profileName = "default",
//                region = "us-east-1"
//            )
//        }

        return DynamoDbClient.fromEnvironment {
            credentialsProvider = StaticCredentialsProvider(
                Credentials(
                    accessKeyId = AWS_KEY!!,
                    secretAccessKey = AWS_SECRET!!,
                    sessionToken = AWS_SESSION!!
                )
            )
            region = REGION!!
        }
    }
}