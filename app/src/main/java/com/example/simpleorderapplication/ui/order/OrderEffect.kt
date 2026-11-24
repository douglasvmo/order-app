package com.example.simpleorderapplication.ui.order

import com.example.simpleorderapplication.domain.Order


sealed interface OrderEffect {
    data class NavigateToOrder(val orderId: String): OrderEffect
    data object GoBackOrders: OrderEffect
    data class ShereOrder(val detachedOrder: Order): OrderEffect
}