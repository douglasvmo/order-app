package com.example.simpleorderapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var clientName: String = "",
    var clientPhone: String = "",
    var date: Date = Date(),
    var status: String = "",
    var type: String = "",
    val total: Long = 0
)