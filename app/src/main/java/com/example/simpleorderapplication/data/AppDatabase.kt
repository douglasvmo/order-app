package com.example.simpleorderapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simpleorderapplication.data.dao.OrderClientDAO
import com.example.simpleorderapplication.data.dao.ProductsDAO
import com.example.simpleorderapplication.data.models.Client
import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.models.Product

@Database(entities = [Client::class, Product::class, Order::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getOrderClientDAO(): OrderClientDAO
    abstract fun getProductDAO(): ProductsDAO

    companion object {
     @Volatile
     private var INSTANCE: AppDatabase? = null

     fun getInstance(context: Context): AppDatabase {
         return INSTANCE ?: synchronized(this) {
             val instance = Room.databaseBuilder(
                 context.applicationContext,
                 AppDatabase::class.java,
                 "order_app_database"
             ).build()
             INSTANCE = instance
             instance
         }

     }
 }
}