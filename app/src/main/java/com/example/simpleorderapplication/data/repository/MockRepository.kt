package com.example.simpleorderapplication.data.repository

import com.example.simpleorderapplication.domain.Order
import com.example.simpleorderapplication.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class MockRepository: OrdersRepository {
    var data = MutableStateFlow<List<Order>>(emptyList())

    override fun observeOrders(): Flow<List<Order>> = flow {
        emitAll(data)
    }

    override suspend fun createOrder(order: Order) {
      data.update {
          it + order
      }
    }

    override suspend fun updateOrder(order: Order) {
        data.update {current ->
            current.map {
                if (order.id == it.id) order else it
            }
        }
    }
}