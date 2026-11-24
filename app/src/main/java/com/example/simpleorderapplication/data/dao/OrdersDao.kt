package com.example.simpleorderapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.simpleorderapplication.data.models.OrderRoomEntity
import com.example.simpleorderapplication.data.models.OrderWithProductsEntity
import com.example.simpleorderapplication.data.models.ProductRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {
    @Transaction
    @Query("SELECT * FROM orders")
    fun observeAll(): Flow<List<OrderWithProductsEntity>>

    @Upsert()
    suspend fun insertOrder(order: OrderRoomEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProducts(products: List<ProductRoomEntity>)

    @Query("DELETE FROM products WHERE orderId = :orderId")
    suspend fun deleteProductsForOrder(orderId: String)

    @Transaction
    suspend fun save(order: OrderWithProductsEntity) {
        insertOrder(order.order)
        deleteProductsForOrder(order.order.id)
        insertProducts(order.products)
    }
}
