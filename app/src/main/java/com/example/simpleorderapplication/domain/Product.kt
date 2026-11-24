package com.example.simpleorderapplication.domain

data class Product(
    val id: String,
    val description: String,
    val quantity: Double,
    val priceCents: Int
)
