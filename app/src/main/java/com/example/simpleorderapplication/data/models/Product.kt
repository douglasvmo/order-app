package com.example.simpleorderapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var id: Long = 0,
    var quantity: Double = 0.0,
    var description: String = "",
    var price: Double = 0.0,

    @ColumnInfo(name = "order_id")
    var orderId: Long = 0
)