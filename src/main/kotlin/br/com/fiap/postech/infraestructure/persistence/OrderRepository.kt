package br.com.fiap.postech.infraestructure.persistence

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import br.com.fiap.postech.domain.entities.Order
import java.util.*

interface OrderRepository {
    suspend fun findById(id: UUID): Map<String, AttributeValue>?
    suspend fun findActiveOrdersSorted(): List<Map<String, AttributeValue>>?
    suspend fun findByStatus(status: String): List<Map<String, AttributeValue>>?
    suspend fun update(id: UUID, newStatus: String): Map<String, AttributeValue>
    suspend fun create(order: Order): Map<String, AttributeValue>?
    suspend fun delete(id: UUID)
}