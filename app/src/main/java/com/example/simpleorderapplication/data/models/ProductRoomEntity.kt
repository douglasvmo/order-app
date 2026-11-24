package com.example.simpleorderapplication.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.simpleorderapplication.domain.Product

@Entity(
    tableName = "products",
    foreignKeys = [ForeignKey(
        entity = OrderRoomEntity::class,
        parentColumns = ["id"],
        childColumns = ["orderId"],
        onDelete = ForeignKey.CASCADE,
    )]
)
data class ProductRoomEntity(
    @PrimaryKey()
    var id: String,
    var orderId: String,
    var description: String,
    var quantity: Double,
    var price: Int
) {
    fun toDomain() = Product(
        id = this.id,
        description = this.description,
        quantity = this.quantity,
        priceCents = this.price,
    )

    companion object {
        fun fromDomain(orderId: String, product: Product) = ProductRoomEntity(
            id = product.id,
            orderId = orderId,
            description = product.description,
            quantity = product.quantity,
            price = product.priceCents
        )

    }
}
