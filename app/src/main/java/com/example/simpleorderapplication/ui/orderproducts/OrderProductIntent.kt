package com.example.simpleorderapplication.ui.orderproducts

sealed interface OrderProductIntent {
    data class LoadProducts(val orderId: String): OrderProductIntent

    data class AddProduct(val description: String, val price:  String, val quantity: String): OrderProductIntent

    data class RemoveProduct(val productId: String): OrderProductIntent

    data object ToggleModal: OrderProductIntent

}