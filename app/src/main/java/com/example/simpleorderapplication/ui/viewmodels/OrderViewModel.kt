package com.example.simpleorderapplication.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.simpleorderapplication.data.AppDatabase
import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.models.Product

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(private val database: AppDatabase) : ViewModel() {

    suspend fun getOrders(): List<Order> = withContext(Dispatchers.IO) {
        runCatching {
            database.getOrderDAO().getAll()
        }.getOrElse {
            it.printStackTrace()
            emptyList()
        }
    }

    suspend fun getProducts(orderId: Long): List<Product> = withContext(Dispatchers.IO) {
        runCatching {
            database.getProductDAO().allProducts(orderId)
        }.getOrElse {
            it.printStackTrace()
            emptyList()
        }
    }


    fun createNewOrder(order: Order) {
            viewModelScope.launch(Dispatchers.IO) {
                runCatching {
                    val orderId = database.getOrderDAO().insert(order)
                    order.apply { id = orderId }
                }.onFailure {
                    it.printStackTrace()
                }
        }

    }

   suspend fun addProducts(product: Product): Product = withContext(Dispatchers.IO) {
       runCatching {
           val productId = database.getProductDAO().insert(product)
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



