package com.example.simpleorderapplication.domain

import java.util.Date

data class Order(
    var id: Long = 0,
    var clientName: String = "",
    var clientPhone: String = "",
    var date: Date = Date(),
    var status: String = "",
    var type: String = "",
    var amount: Long = 0,
    var products: List<Product> = emptyList()
)
