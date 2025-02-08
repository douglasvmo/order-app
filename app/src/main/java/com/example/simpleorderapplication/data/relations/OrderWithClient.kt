package com.example.simpleorderapplication.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.simpleorderapplication.data.models.Client
import com.example.simpleorderapplication.data.models.Order

data class OrderWithClient (
    @Embedded val client: Client,
    @Relation(parentColumn = "client_id", entityColumn = "client_id")
    val order: Order
)