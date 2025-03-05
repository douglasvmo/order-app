package com.example.simpleorderapplication.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.simpleorderapplication.data.AppDatabase
import com.example.simpleorderapplication.data.models.Client
import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.models.Product
import com.example.simpleorderapplication.data.relations.OrderWithClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(private val database: AppDatabase) : ViewModel() {
    private val _orders = MutableLiveData<List<OrderWithClient>>(listOf())
    val orders = _orders

    private val _currentOrder = MutableLiveData<OrderWithClient>();
    val currentOrder = _currentOrder

    private val _products = MutableLiveData<List<Product>>();
    val products = _products

    fun getNextOrderNumber(): Int {
        return _orders.value!!.size + 1
    }

    fun loadOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                database.getOrderClientDAO().getAll()
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    _orders.value = it
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

   fun  getCurrentOrderProducts(){
       val orderId = _currentOrder.value?.order?.id!!
       viewModelScope.launch(Dispatchers.IO) {
           runCatching {
               database.getProductDAO().allProducts(orderId)
           }.onSuccess {
               _products.postValue(it)
           }.onFailure {
               it.printStackTrace()
           }
       }
   }

    fun createNewOrder(client: Client) {
            viewModelScope.launch(Dispatchers.IO) {
                runCatching {
                    if(client.id == 0L){
                        val clientId = database.getOrderClientDAO().insert(client)
                        client.id = clientId
                    }
                    val order = Order(clientId = client.id)
                    val orderId = database.getOrderClientDAO().insert(order)
                    order.apply { id = orderId }
                    OrderWithClient(client, order)
                }.onSuccess {
                    withContext(Dispatchers.Main){
                        _currentOrder.postValue(it)
                    }
                }.onFailure {
                    it.printStackTrace()
                }
        }

    }

    fun addProducts(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                database.getProductDAO().insert(product)
            }.onSuccess {

            }
        }

    }

    fun selectOrder(order: OrderWithClient) {
        _currentOrder.postValue(order)
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



