package br.com.fiap.postech.domain

import br.com.fiap.postech.domain.entities.Order
import br.com.fiap.postech.domain.entities.OrderStatus


object OrderStatusComparatorProvider {
    fun getComparator(): Comparator<Order> {
        return object : Comparator<Order> {
            override fun compare(o1: Order, o2: Order): Int {
                return statusPriority(o1.status).compareTo(statusPriority(o2.status))
            }

            private fun statusPriority(status: OrderStatus): Int {
                return when (status) {
                    OrderStatus.READY -> 1
                    OrderStatus.IN_PREPARATION -> 2
                    OrderStatus.RECEIVED -> 3
                    else -> 4
                }
            }
        }
    }


}