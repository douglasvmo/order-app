package com.example.simpleorderapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var clientName: String = "",
    var clientPhone: String = "",
    var date: Date = Date(),
    var status: String = "",
    var type: String = "",
    var amount: Long = 0
)