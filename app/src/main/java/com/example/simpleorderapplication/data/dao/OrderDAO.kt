package com.example.simpleorderapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.simpleorderapplication.data.models.OrderEntity
import com.example.simpleorderapplication.data.models.OrderWithProducts

@Dao
interface OrderDAO {

    @Insert
    fun insert(order: OrderEntity): Long

    @Delete
    fun delete(order: OrderEntity)

    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAll(): List<OrderEntity>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :id")
    fun getOrderWithProducts(id: Long): OrderWithProducts

    @Query("UPDATE orders SET  amount = :amount WHERE id = :orderId")
    fun updateAmount(orderId: Long, amount: Long)

}