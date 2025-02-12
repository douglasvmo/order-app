package com.example.simpleorderapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.simpleorderapplication.data.models.Client
import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.relations.OrderWithClient

@Dao
interface OrderClientDAO {

    @Insert
    fun insert(order: Order): Long

    @Insert
    fun insert(client: Client): Long

    @Delete
    fun delete(order: Order)

    @Delete
    fun delete(client: Client)

    @Query(
        """
            SELECT * FROM orders o 
           INNER JOIN clients c ON o.client_id = c.client_id
           WHERE o.order_id = :orderId
           """
    )
    fun findById(orderId: Long): OrderWithClient?

    @Query(
        """
            SELECT * FROM orders o 
           INNER JOIN clients c ON o.client_id = c.client_id
           """
    )
    fun getAll(): List<OrderWithClient>?


}