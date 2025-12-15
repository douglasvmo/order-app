package com.example.simpleorderapplication.ui.orderproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleorderapplication.data.models.Product
import com.example.simpleorderapplication.data.repository.OrderRepository
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderProductsViewModel( private  val repository: OrderRepository) : ViewModel() {
    private val _state = MutableStateFlow(OrderProductsState())
    val state: MutableStateFlow<OrderProductsState> = _state


    fun dispatch(intent: OrderProductIntent){
        when(intent){
            is OrderProductIntent.LoadProducts -> loadProducts(intent.orderId)
            is OrderProductIntent.AddProduct -> addProduct(intent)
            is OrderProductIntent.RemoveProduct -> removeProduct(RealmUUID.from(intent.productId))
            is OrderProductIntent.ToggleModal -> toggleModal()
        }
    }

    private fun loadProducts(orderId: String){
        viewModelScope.launch {
            runCatching {
                repository.observeSingleOrder(orderId)
            }.onSuccess { flow ->
                flow.collect { order ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            order = order
                        )
                    }
                }

            }
        }

    }

    private fun addProduct(intent: OrderProductIntent.AddProduct){
        _state.update { it.copy(isLoading = true ) }
        val orderId = _state.value.order!!.id

        val product = Product().apply {
            description = intent.description
            priceCents = intent.price.replace(",", ".").toDoubleOrNull()?.times(100)?.toLong() ?: 0L
            quantity = intent.quantity.replace(",", ".").toDoubleOrNull() ?: 1.0
        }

        viewModelScope.launch {
            runCatching {
                repository.addProduct(orderId, product)
            }.onFailure { erro ->
                _state.update { it.copy(error = erro.message, isLoading = false)  }
            }
        }


    }

    private fun removeProduct(productId: RealmUUID) {
        _state.update { it.copy(isLoading = true) }
        val orderId = _state.value.order!!.id

        viewModelScope.launch {
            runCatching {
                repository.removeProduct(orderId, productId)
            }.onFailure {
                _state.update { it.copy(error = it.error, isLoading = false) }
            }
        }

    }

    private fun toggleModal(){
        _state.update { it.copy(showModal = !it.showModal) }
    }



}