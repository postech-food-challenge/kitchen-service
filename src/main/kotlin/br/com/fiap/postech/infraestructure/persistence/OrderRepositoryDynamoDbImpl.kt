package br.com.fiap.postech.infraestructure.persistence


import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderStatus
import br.com.fiap.postech.infraestructure.aws.IDynamoDbClientProvider
import software.amazon.awssdk.services.dynamodb.model.*
import java.util.*

class OrderRepositoryDynamoDbImpl(private val provider: IDynamoDbClientProvider) : OrderRepository {

    companion object {
        const val TABLE_NAME = "orders"
        const val KEY_COLUMN_NAME = "id"
    }

    override suspend fun findById(id: UUID): Map<String, AttributeValue>? {
        val client = provider.getClient()

        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet[KEY_COLUMN_NAME] = AttributeValue.builder().s(id.toString()).build()

        val request = GetItemRequest.builder()
            .key(keyToGet)
            .tableName(TABLE_NAME)
            .build()

        val returnedItem = client.getItem(request)
        return returnedItem.item()
    }

    override suspend fun findActiveOrdersSorted(): List<Map<String, AttributeValue>>? {
        val client = provider.getClient()

        val aliases = mapOf("#s" to "status")

        val request = ScanRequest.builder()
            .tableName(TABLE_NAME)
            .filterExpression("#s <> :completed AND #s <> :canceled")
            .expressionAttributeNames(aliases)
            .expressionAttributeValues(
                mapOf(
                    ":completed" to AttributeValue.builder().s(OrderStatus.COMPLETED.name).build(),
                    ":canceled" to AttributeValue.builder().s(OrderStatus.CANCELED.name).build()
                )
            )
            .build()

        val scanResponse = client.scan(request)
        return scanResponse.items()
    }

    override suspend fun findByStatus(status: String): List<Map<String, AttributeValue>>? {
        val client = provider.getClient()

        val aliases = mapOf("#s" to "status")

        val request = ScanRequest.builder()
            .tableName(TABLE_NAME)
            .filterExpression("#s = :status")
            .expressionAttributeNames(aliases)
            .expressionAttributeValues(mapOf(":status" to AttributeValue.builder().s(status).build()))
            .build()

        val scanResponse = client.scan(request)
        return scanResponse.items()
    }

    override suspend fun update(id: UUID, newStatus: String): Map<String, AttributeValue> {
        val client = provider.getClient()

        val itemKey = mutableMapOf<String, AttributeValue>()
        itemKey[KEY_COLUMN_NAME] = AttributeValue.builder().s(id.toString()).build()

        val updatedValues = mutableMapOf<String, AttributeValueUpdate>()
        updatedValues["status"] = AttributeValueUpdate.builder()
            .value(AttributeValue.builder().s(newStatus).build())
            .action(AttributeAction.PUT)
            .build()

        val request = UpdateItemRequest.builder()
            .tableName(TABLE_NAME)
            .key(itemKey)
            .attributeUpdates(updatedValues)
            .build()
        client.updateItem(request)

        return findById(id)!!
    }

    override suspend fun create(order: Order): Map<String, AttributeValue>? {
        val client = provider.getClient()

        val request = PutItemRequest.builder()
            .tableName(TABLE_NAME)
            .item(Order.toMap(order))
            .build()

        client.putItem(request)

        return findById(order.id)
    }

    override suspend fun delete(id: UUID) {
        val client = provider.getClient()

        val keyToGet = mutableMapOf<String, AttributeValue>()
        keyToGet[KEY_COLUMN_NAME] = AttributeValue.builder().s(id.toString()).build()

        val request = DeleteItemRequest.builder()
            .tableName(TABLE_NAME)
            .key(keyToGet)
            .build()

        client.deleteItem(request)
    }
}