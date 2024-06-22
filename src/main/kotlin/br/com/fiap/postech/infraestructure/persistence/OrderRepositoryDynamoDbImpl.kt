package br.com.fiap.postech.infraestructure.persistence

import aws.sdk.kotlin.services.dynamodb.model.*
import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.infraestructure.aws.IDynamoDbClientProvider
import java.util.*

class OrderRepositoryDynamoDbImpl(private val provider: IDynamoDbClientProvider) : OrderRepository {

    companion object {
        const val TABLE_NAME = "orders"
        const val KEY_COLUMN_NAME = "id"
    }

    override suspend fun findById(id: UUID): Map<String, AttributeValue>? {
        val client = provider.getClient()

        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet[KEY_COLUMN_NAME] = AttributeValue.S(id.toString())

        val request = GetItemRequest {
            key = keyToGet
            tableName = TABLE_NAME
        }

        val returnedItem = client.getItem(request)

        return returnedItem.item
    }

    override suspend fun findActiveOrdersSorted(): List<Map<String, AttributeValue>>? {
        val client = provider.getClient()

        val aliases = mapOf("#s" to "status")

        val request = ScanRequest {
            tableName = TABLE_NAME
            filterExpression = "#s <> :completed AND #s <> :canceled"
            expressionAttributeNames = aliases
            expressionAttributeValues = mapOf(
                ":completed" to AttributeValue.S(OrderStatus.COMPLETED.name),
                ":canceled" to AttributeValue.S(OrderStatus.CANCELED.name)
            )
        }

        val scanResponse = client.scan(request)
        return scanResponse.items
    }

    override suspend fun findByStatus(status: String): List<Map<String, AttributeValue>>? {
        val client = provider.getClient()

        val aliases = mapOf("#s" to "status")

        val request = ScanRequest {
            tableName = TABLE_NAME
            filterExpression = "#s = :status"
            expressionAttributeNames = aliases
            expressionAttributeValues = mapOf(":status" to AttributeValue.S(status))
        }

        val scanResponse = client.scan(request)
        return scanResponse.items
    }

    override suspend fun update(id: UUID, newStatus: String): Map<String, AttributeValue> {
        val client = provider.getClient()

        val itemKey = mutableMapOf<String, AttributeValue>()
        itemKey[KEY_COLUMN_NAME] = AttributeValue.S(id.toString())

        val updatedValues = mutableMapOf<String, AttributeValueUpdate>()
        updatedValues["status"] = AttributeValueUpdate {
            value = AttributeValue.S(newStatus)
            action = AttributeAction.Put
        }

        val request = UpdateItemRequest {
            tableName = TABLE_NAME
            key = itemKey
            attributeUpdates = updatedValues
        }
        client.updateItem(request)

        return findById(id)!!
    }

    override suspend fun create(order: Order): Map<String, AttributeValue>? {
        val client = provider.getClient()

        val request = PutItemRequest {
            tableName = "orders"
            item = Order.toMap(order)
        }

        client.putItem(request)

        return  findById(order.id)
    }

    override suspend fun delete(id: UUID) {
        val client = provider.getClient()

        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet[KEY_COLUMN_NAME] = AttributeValue.S(id.toString())

        val request = DeleteItemRequest {
            tableName = TABLE_NAME
            key = keyToGet
        }

        client.deleteItem(request)
    }
}