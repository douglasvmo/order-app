package com.example.simpleorderapplication.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simpleorderapplication.data.AppDatabase
import com.example.simpleorderapplication.data.models.OrderEntity
import com.example.simpleorderapplication.data.models.ProductEntity
import com.example.simpleorderapplication.domain.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderViewModel(private val database: AppDatabase) : ViewModel() {

    suspend fun getOrders(): List<OrderEntity> = withContext(Dispatchers.IO) {
        runCatching {
            database.getOrderDAO().getAll()
        }.getOrElse {
            it.printStackTrace()
            emptyList()
        }
    }

    suspend fun getOrder(orderId: Long): Order = withContext(Dispatchers.IO) {
        runCatching {
            database.getOrderDAO().getOrderWithProducts(orderId).toDomain()
        }.getOrElse {
            it.printStackTrace()
            Order()
        }
    }


    suspend fun createNewOrder(order: OrderEntity): OrderEntity = withContext(Dispatchers.IO) {
        runCatching {
            val orderId = database.getOrderDAO().insert(order)
            order.apply { id = orderId }
        }.getOrElse {
            it.printStackTrace()
            order
        }
    }

   suspend fun addProducts(product: ProductEntity): ProductEntity = withContext(Dispatchers.IO) {
       runCatching {
           val productId = database.getProductDAO().insert(product)
           val products = database.getProductDAO().allProducts(product.orderId)
           val amount = products.sumOf { it.quantity * it.price }
           database.getOrderDAO().updateAmount(product.orderId, amount)

           product.apply { id = productId }
       }.getOrElse {
           it.printStackTrace()
           product
       }
    }

    class OrderViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OrderViewModel(AppDatabase.getInstance(context)) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}



