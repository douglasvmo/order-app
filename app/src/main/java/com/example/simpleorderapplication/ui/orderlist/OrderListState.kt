package com.example.simpleorderapplication.ui.orderlist

import com.example.simpleorderapplication.data.models.Order

data class OrderListState (
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val showModal: Boolean = false,
    val erro: String? = null
)