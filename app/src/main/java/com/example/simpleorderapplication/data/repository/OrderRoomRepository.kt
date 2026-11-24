package com.example.simpleorderapplication.data.repository

import com.example.simpleorderapplication.data.dao.OrdersDao
import com.example.simpleorderapplication.data.RealmToRoomMigrator
import com.example.simpleorderapplication.data.models.OrderRoomEntity
import com.example.simpleorderapplication.data.models.OrderWithProductsEntity
import com.example.simpleorderapplication.domain.Order
import com.example.simpleorderapplication.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OrderRoomRepository(
    private val ordersDao: OrdersDao,
    private val migrator: RealmToRoomMigrator
): OrdersRepository {
    override fun observeOrders(): Flow<List<Order>> = flow {
        migrator.migrateIfNeeded()
        emitAll(ordersDao.observeAll().map { list -> list.map { it.toDomain() } })
    }

    override suspend fun createOrder(order: Order) {
        ordersDao.insertOrder(OrderRoomEntity.fromDomain(order))
    }

    override suspend fun updateOrder(order: Order) {
        ordersDao.save(OrderWithProductsEntity.fromDomain(order))
    }

}
