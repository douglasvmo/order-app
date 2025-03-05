package com.example.simpleorderapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.simpleorderapplication.data.models.Client

@Dao
interface ClientDAO {

    @Insert
    suspend fun insert(client: Client): Long

    @Query("SELECT * FROM products WHERE order_id = :clientId")
    suspend fun findById(clientId: Long): Client?
}