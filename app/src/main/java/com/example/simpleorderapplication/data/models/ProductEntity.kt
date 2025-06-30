package com.example.simpleorderapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.simpleorderapplication.domain.Product


@Entity(tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["order_id"])])
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var id: Long = 0,
    var quantity: Int = 0,
    var description: String = "",
    var price: Long = 0,

    @ColumnInfo(name = "order_id") var orderId: Long = 0
) {
    fun toDomain() = Product(
        id = this.id,
        quantity = this.quantity,
        description = this.description,
        price = this.price
    )
}