package com.example.simpleorderapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.simpleorderapplication.data.models.Client
import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.relations.OrderWithClient

@Dao
interface OrderClientDAO {

    @Insert
    fun insert(order: Order): Long

    @Insert
    fun insert(client: Client): Long

    fun insert(client: Client, order: Order): Long {
        val clientId = insert(client)
        order.clientId = clientId.toInt()
        return insert(order)
    }

    @Transaction
    @Query("SELECT * FROM orders o INNER JOIN clients c ON o.client_id = c.client_id")
    fun getAll(): List<OrderWithClient>

    @Query("SELECT * FROM clients")
    fun getAllClients(): List<Client>

    @Query("SELECT * FROM orders")
    fun getAllOrders(): List<Order>

}