package com.example.simpleorderapplication.domain.repository

import com.example.simpleorderapplication.domain.Order
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    fun observeOrders(): Flow<List<Order>>

    suspend fun createOrder(order: Order)

    suspend fun updateOrder(order: Order)
}
