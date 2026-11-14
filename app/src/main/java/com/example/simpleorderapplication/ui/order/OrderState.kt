package com.example.simpleorderapplication.ui.order

import com.example.simpleorderapplication.data.models.Order

data class OrderState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val currentOrder: Order? = null,
    val erro: String? = null
)
