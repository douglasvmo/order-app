package com.example.simpleorderapplication.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.models.Product
import com.example.simpleorderapplication.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(private val  repository: OrderRepository): ViewModel() {

    private val _state = MutableStateFlow(OrderState())
    val state: StateFlow<OrderState> = _state
        .onStart { loadOrders() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OrderState()
        )

    private val _effect = MutableSharedFlow<OrderEffect>()
    val effect: MutableSharedFlow<OrderEffect> = _effect

    fun dispatch (intent: OrderAction){
        when(intent){
            is OrderAction.GoBackOrders -> goBackOrder()
            is OrderAction.SelectOrder -> selectOrder(intent)
            is OrderAction.CreateOrder -> createOrder(intent)
            is OrderAction.AddProduct -> addProduct(intent)
            is OrderAction.Shere -> shere()
        }
    }

    private fun loadOrders(){

        viewModelScope.launch {
            runCatching {
                repository.observeOrders()
            }.onSuccess { flow ->
                flow.collect { orders ->
                    _state.update { state ->
                        state.copy(
                            orders = orders,
                            currentOrder = orders.firstOrNull { it.id == state.currentOrder?.id},
                            isLoading = false)
                    }
                }
            }.onFailure {
                _state.update {
                    it.copy(erro = it.erro, isLoading = false)
                }
            }


        }

    }

    private fun createOrder(intent: OrderAction.CreateOrder){
        viewModelScope.launch {
            runCatching {
                repository.createOrder(
                    Order().apply {
                        clientName = intent.clientName
                        clientPhone = intent.clientPhone
                        clientCode = intent.clientCPF
                    }
                )
            }
                .onSuccess {
                    selectOrder(OrderAction.SelectOrder(it))
                }
                .onFailure {
                    _state.update { it.copy(erro = it.erro, isLoading = false) }
                }
        }
    }

    private fun addProduct(intent: OrderAction.AddProduct){
        _state.update { it.copy(isLoading = true ) }
        val orderId = _state.value.currentOrder?.id ?: return

        val product = Product().apply {
            description = intent.description
            priceCents = intent.price.replace(",", ".").toDoubleOrNull()?.times(100)?.toLong() ?: 0L
            quantity = intent.quantity.replace(",", ".").toDoubleOrNull() ?: 1.0
        }

        viewModelScope.launch {
            runCatching {
                repository.addProduct(orderId, product)
            }.onFailure { erro ->
                _state.update { it.copy(erro = erro.message, isLoading = false)  }
            }
        }
    }


    private fun selectOrder(intent: OrderAction.SelectOrder){
        val order = _state.value.orders.find { it -> it.id.toString() == intent.orderId } ?: return

        _state.update { it.copy( currentOrder = order) }
        viewModelScope.launch {
            _effect.emit(OrderEffect.NavigateToOrder(order.id.toString()))
        }
    }

    private fun goBackOrder(){
        viewModelScope.launch { _effect.emit(OrderEffect.GoBackOrders) }
    }

    private fun shere(){
        val managedOrder = _state.value.currentOrder ?: return

        viewModelScope.launch {
            runCatching {
                repository.detach(managedOrder)
            }.onSuccess {detachedOrder ->
                _effect.emit(OrderEffect.ShereOrder(detachedOrder))
            }.onFailure {
                _state.update { it.copy( erro = it.erro.toString()) }
            }
        }
    }

}