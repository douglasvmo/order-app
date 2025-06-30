package com.example.simpleorderapplication.domain

data class Product(
    var id: Long = 0,
    var quantity: Int = 0,
    var description: String = "",
    var price: Long = 0,
)
