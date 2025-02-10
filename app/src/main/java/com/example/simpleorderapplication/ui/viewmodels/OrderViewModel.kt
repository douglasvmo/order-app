package com.example.simpleorderapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    val orders: LiveData<List<OrderWithClient>> = _orders

    private val _currentOrder = MutableLiveData<Order>();
    val currentOrder: LiveData<Order> = _currentOrder

    fun getNextOrderNumber(): Int {
        return _orders.value!!.size + 1
    }

    fun loadOrders() {

    }

    fun createNewOrder(client: Client) {
            viewModelScope.launch {
                runCatching {
                    if(client.id == 0L){
                        val clientId = database.getClientDAO().insert(client)
                        client.id = clientId
                    }
                    val order = Order(clientId = client.id)
                    val orderId = database.getOrderDAO().insert(order)
                    order.apply { id = orderId }
                }.onSuccess {
                    withContext(Dispatchers.IO) {
                        _currentOrder.postValue(it)
                    }
                }.onFailure {
                    it.printStackTrace()
                }
        }

    }

    fun addProducts(product: Product) {
        viewModelScope.launch {
            runCatching {
                database.getProductDAO().insert(product)
            }.onSuccess {

            }
        }

    }

}



