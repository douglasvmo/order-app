package com.example.simpleorderapplication.data.repository

import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.models.Product
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepository(private val realm: Realm) {

    fun observeOrders(): Flow<List<Order>>{
      return realm.query<Order>()
            .sort("code", Sort.DESCENDING)
            .asFlow()
            .map { change ->
                when (change) {
                    is InitialResults -> change.list
                    is UpdatedResults -> change.list
                }
            }
    }

    fun observeSingleOrder(orderId: String): Flow<Order?> {
       return realm.query<Order>("id = $0", RealmUUID.from(orderId))
           .first()
           .asFlow()
           .map { it.obj }
    }

    suspend fun createOrder(order: Order){
        realm.write {
            val count = realm.query<Order>()
                .sort("id")
                .count()
                .find()

            copyToRealm(order.apply {
                code = (count.toInt() + 1)
            })
        }
    }

    suspend fun addProduct(orderId: RealmUUID, product: Product) {
        realm.write {
            val order = query<Order>("id = $0", orderId).first().find()
            order?.products?.add(product)
        }
    }

    suspend fun removeProduct(orderId: RealmUUID, productId: RealmUUID)  {
        realm.write {
            val order = query<Order>("id = $0", orderId).first().find()
            if (order != null) {
                val product = order.products.find { it.id == productId }
                if (product != null) {
                    delete(product)
                }
            }
        }
    }

}