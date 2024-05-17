package com.example.infraestructure.persistence

import com.example.infraestructure.persistence.entities.Orders
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SqlExpressionBuilder.case
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.intLiteral

fun statusCustomOrdering(): Expression<Int> {
    return case()
        .When(Orders.status eq "READY", intLiteral(1))
        .When(Orders.status eq "IN_PREPARATION", intLiteral(2))
        .When(Orders.status eq "RECEIVED", intLiteral(3))
        .Else(intLiteral(4))
}