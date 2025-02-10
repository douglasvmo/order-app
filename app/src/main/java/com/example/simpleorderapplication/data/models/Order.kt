package com.example.simpleorderapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "orders",
    foreignKeys = [ForeignKey(
        entity = Client::class,
        parentColumns = ["client_id"],
        childColumns = ["client_id"],
        onDelete = ForeignKey.NO_ACTION
    )],
    indices = [Index("client_id")]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    var id: Long = 0,

    @ColumnInfo(name = "client_id")
    var clientId: Long = 0,

    var date: Date = Date(),
    var status: String = "",
    var type: String = "",
    var total: Double = 0.0
)