package com.example.simpleorderapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.simpleorderapplication.data.models.Order

@Dao
interface OrderDAO {

    @Insert
    fun insert(order: Order): Long

    @Query("SELECT * FROM orders WHERE order_id = :orderId")
    fun findById(orderId: Long): Order?
}