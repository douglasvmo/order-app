package com.example.simpleorderapplication.data.repository

import com.example.simpleorderapplication.data.models.RealmOrder
import com.example.simpleorderapplication.data.models.RealmProduct
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRealmRepository(private val realm: Realm) {

    fun observeOrders(): Flow<List<RealmOrder>>{
      return realm.query<RealmOrder>()
            .sort("code", Sort.DESCENDING)
            .asFlow()
            .map { change ->
                when (change) {
                    is InitialResults -> change.list
                    is UpdatedResults -> change.list
                }
            }
    }

    fun observeSingleOrder(orderId: String): Flow<RealmOrder?> {
       return realm.query<RealmOrder>("id = $0", RealmUUID.from(orderId))
           .first()
           .asFlow()
           .map { it.obj }
    }

    suspend fun createOrder(order: RealmOrder): String {
        return realm.write {
            val count = realm.query<RealmOrder>()
                .sort("id")
                .count()
                .find()

            val managedOrder = copyToRealm(order.apply {
                code = (count.toInt() + 1)
            })

            managedOrder.id.toString()

        }
    }

    suspend fun addProduct(orderId: RealmUUID, product: RealmProduct) {
        realm.write {
            val order = query<RealmOrder>("id = $0", orderId).first().find()
            order?.products?.add(product)
        }
    }

    suspend fun removeProduct(orderId: RealmUUID, productId: RealmUUID)  {
        realm.write {
            val order = query<RealmOrder>("id = $0", orderId).first().find()
            if (order != null) {
                val product = order.products.find { it.id == productId }
                if (product != null) {
                    delete(product)
                }
            }
        }
    }


    fun <T : io.realm.kotlin.types.TypedRealmObject> detach(obj: T): T {
        return realm.copyFromRealm(obj)
    }



}