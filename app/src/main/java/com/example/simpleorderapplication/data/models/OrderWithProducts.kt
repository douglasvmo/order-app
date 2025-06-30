package com.example.simpleorderapplication.data.models


import androidx.room.Embedded
import androidx.room.Relation
import com.example.simpleorderapplication.domain.Order

data class OrderWithProducts(
    @Embedded val order: OrderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "order_id"

    )
    val products: List<ProductEntity>
) {
    fun toDomain() = Order(
        id = this.order.id,
        clientName = this.order.clientName,
        clientPhone = this.order.clientPhone,
        date = this.order.date,
        type = this.order.type,
        amount = this.order.amount,
        products = this.products.map { it.toDomain() }
    )
}
