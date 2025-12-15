package com.example.simpleorderapplication.ui.orderlist

sealed class OrderListIntent {
    data object LoadOrders: OrderListIntent()

    data class CreateOrder(val clientName: String, val clientPhone: String, val clientCPF: String): OrderListIntent()

    data class OrderSelect(val id: String): OrderListIntent()

    data object toggleModal: OrderListIntent()
}