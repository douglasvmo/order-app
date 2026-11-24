package com.example.simpleorderapplication.ui.order

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleorderapplication.data.RealmToRoomMigrator
import com.example.simpleorderapplication.domain.Client
import com.example.simpleorderapplication.domain.Order
import com.example.simpleorderapplication.domain.Product

import com.example.simpleorderapplication.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.text.toDoubleOrNull
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OrderViewModel(private val  repository: OrdersRepository): ViewModel() {

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
            }.onFailure { error ->
                _state.update {
                    it.copy(erro = error.message, isLoading = false)
                }
            }


        }

    }



    @OptIn(ExperimentalUuidApi::class)
    private fun createOrder(intent: OrderAction.CreateOrder){
        val id = Uuid.random().toString()
        viewModelScope.launch {
            runCatching {
                repository.createOrder(
                    Order(
                        id,
                        code = _state.value.orders.size.plus(1).toString(),
                        clientName = intent.clientName,
                        clientCode = intent.clientCPF,
                        clientPhone = intent.clientPhone,
                        products = listOf()
                    )
                )
            }
                .onSuccess {
                    selectOrder(OrderAction.SelectOrder(id))
                }
                .onFailure { error ->
                    _state.update { it.copy(erro = error.message, isLoading = false) }
                }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun addProduct(intent: OrderAction.AddProduct){
        val order = _state.value.currentOrder ?: return
        val product = Product(
            id = Uuid.random().toString(),
            description = intent.description,
            priceCents = intent.price.replace(",", ".").toBigDecimalOrNull()
                ?.movePointRight(2)
                ?.toInt() ?: 0,
            quantity = intent.quantity.replace(",", ".").toDoubleOrNull() ?: 1.0
        )
        val updatedOrder = order.copy(products = order.products + product)

        _state.update { it.copy(isLoading = true, erro = null) }
        viewModelScope.launch {
            runCatching {
                repository.updateOrder(updatedOrder)
            }.onSuccess {
                _state.update { it.copy( isLoading = false) }
            }.onFailure { erro ->
                _state.update { it.copy(erro = erro.message, isLoading = false)  }

            }
        }
    }


    private fun selectOrder(intent: OrderAction.SelectOrder){
        val order = _state.value.orders.find { it -> it.id == intent.orderId } ?: return

        _state.update { it.copy( currentOrder = order) }
        viewModelScope.launch {
            _effect.emit(OrderEffect.NavigateToOrder(order.id))
        }
    }

    private fun goBackOrder(){
        viewModelScope.launch { _effect.emit(OrderEffect.GoBackOrders) }
    }

    private fun shere(){
        val orderSnapshot = _state.value.currentOrder ?: return

        viewModelScope.launch {
            runCatching {
                _effect.emit(OrderEffect.ShereOrder(orderSnapshot))
            }.onFailure {
                _state.update { state -> state.copy(erro = it.message) }
            }
        }
    }

}
