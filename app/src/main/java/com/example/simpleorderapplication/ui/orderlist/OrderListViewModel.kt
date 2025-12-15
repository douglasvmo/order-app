package com.example.simpleorderapplication.ui.orderlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleorderapplication.data.models.Order
import com.example.simpleorderapplication.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderListViewModel(private val repository: OrderRepository): ViewModel() {
    private val _state = MutableStateFlow(OrderListState())
    val state: StateFlow<OrderListState> = _state

    private val _effect = MutableSharedFlow<OrderListEffect>()
    val effect: MutableSharedFlow<OrderListEffect> = _effect


    fun dispath(intent: OrderListIntent){
        when(intent){
            is OrderListIntent.LoadOrders -> loadOrders()
            is OrderListIntent.CreateOrder -> createOrder(intent)

            is OrderListIntent.OrderSelect -> selectOrder(intent.id)
            is OrderListIntent.toggleModal -> toggleModal()
        }
    }

    private fun loadOrders(){

        viewModelScope.launch {
            runCatching {
                repository.observeOrders()
            }.onSuccess { flow ->
                flow.collect { orders ->
                    _state.update {
                        it.copy( orders = orders, isLoading = false)
                    }
                }
            }.onFailure {
                _state.update {
                    it.copy(erro = it.erro, isLoading = false)
                }
            }


        }

    }

    private fun createOrder(intent: OrderListIntent.CreateOrder){
        viewModelScope.launch {
            runCatching {
                repository.createOrder(
                    Order().apply {
                        clientName = intent.clientName
                        clientPhone = intent.clientPhone
                        clientCode = intent.clientCPF
                    }
                )
            }.onFailure {
                _state.update { it.copy(erro = it.erro, isLoading = false) }
            }
        }
        

    }

    private fun selectOrder(id: String) {
        viewModelScope.launch {
            _effect.emit(OrderListEffect.NavigateToOrderProducts(id))
        }

    }

    private fun toggleModal() {
        _state.update { it.copy(showModal = !it.showModal) }
    }
}