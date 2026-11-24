package com.example.simpleorderapplication.ui.order

sealed interface OrderAction {
    data class SelectOrder(val orderId: String) : OrderAction
    data class CreateOrder(val clientName: String, val clientPhone: String, val clientCPF: String): OrderAction
    data class AddProduct(val description: String, val price:  String, val quantity: String): OrderAction
    data object GoBackOrders : OrderAction
    data object Shere: OrderAction

}