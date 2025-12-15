package com.example.simpleorderapplication.ui.orderproducts

import com.example.simpleorderapplication.data.models.Order

data class OrderProductsState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val error: String? = null,
    val showModal: Boolean = false
)
