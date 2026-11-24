package com.example.simpleorderapplication.data.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.simpleorderapplication.domain.Order
import java.time.LocalDate

data class OrderWithProductsEntity(
    @Embedded
    val order: OrderRoomEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val products: List<ProductRoomEntity>
) {
    fun toDomain() = Order(
        id = this.order.id,
        code = this.order.code,
        clientName = order.clientName,
        clientCode = this.order.clientCode,
        clientPhone =  this.order.clientPhone,
        products = this.products.map {  it.toDomain() },
        createAt = this.order.createAt
    )

    companion object {
       fun fromDomain(order: Order) = OrderWithProductsEntity(
           order = OrderRoomEntity.fromDomain(order),
           products = order.products.map { ProductRoomEntity.fromDomain(order.id, it) }
       )
    }
}
