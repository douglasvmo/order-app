package com.example.simpleorderapplication.ui.orderlist

sealed interface OrderListEffect {
    data class NavigateToOrderProducts(val orderId: String): OrderListEffect
}