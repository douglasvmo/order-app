package com.example.simpleorderapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val id: Int,
    var name: String,
    var description: String,
    var price: Double,

    @ColumnInfo(name = "order_id")
    val orderId: Int
)