package com.example.simpleorderapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simpleorderapplication.domain.Order
import java.time.LocalDate

@Entity(tableName = "orders")
data class OrderRoomEntity(
    @PrimaryKey()
    var id: String,
    var code: String,
    val clientCode: String?,
    val clientName: String,
    val clientPhone: String?,
    var createAt: LocalDate
) {

    companion object {
        fun fromDomain(order: Order) = OrderRoomEntity(
            id = order.id,
            code = order.code,
            clientCode = order.clientCode,
            clientName = order.clientName,
            clientPhone = order.clientPhone,
            createAt = order.createAt ?: LocalDate.now()
        )
    }
}
