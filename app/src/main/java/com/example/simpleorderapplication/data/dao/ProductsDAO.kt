package com.example.simpleorderapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.simpleorderapplication.data.models.Product

@Dao
interface ProductsDAO {

    @Insert
    fun insert(product: Product): Long
}