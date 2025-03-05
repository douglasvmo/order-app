package com.example.simpleorderapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.models.Product

@Dao
interface ProductsDAO {

    @Insert
    fun insert(product: Product): Long

    @Query("SELECT * FROM products WHERE order_id = :orderId")
    fun allProducts(orderId: Long): List<Product>
}