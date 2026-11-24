package com.example.simpleorderapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simpleorderapplication.data.dao.OrdersDao
import com.example.simpleorderapplication.data.models.OrderRoomEntity
import com.example.simpleorderapplication.data.models.ProductRoomEntity

@Database(
    entities = [
        OrderRoomEntity::class,
        ProductRoomEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ordersDao(): OrdersDao
}
