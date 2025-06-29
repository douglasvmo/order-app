package com.example.simpleorderapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.simpleorderapplication.data.models.Order

@Dao
interface OrderDAO {

    @Insert
    fun insert(order: Order): Long

    @Delete
    fun delete(order: Order)

    @Query("SELECT * FROM orders")
    fun getAll(): List<Order>


}