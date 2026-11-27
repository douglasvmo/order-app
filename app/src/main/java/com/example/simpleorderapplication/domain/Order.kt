package com.example.simpleorderapplication.domain


import java.time.LocalDate

data class Order(
    var id: String,
    var code: String = "0",
    var clientName: String,
    var clientCode: String? = null,
    var clientPhone: String? = null,
    var products: List<Product>,
    var createAt: LocalDate? = null
)