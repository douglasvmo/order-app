package com.example.simpleorderapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simpleorderapplication.data.dao.OrderDAO
import com.example.simpleorderapplication.data.dao.ProductsDAO
import com.example.simpleorderapplication.data.models.OrderEntity
import com.example.simpleorderapplication.data.models.ProductEntity

@Database(entities = [ProductEntity::class, OrderEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getOrderDAO(): OrderDAO
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
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }

        }
 }
}