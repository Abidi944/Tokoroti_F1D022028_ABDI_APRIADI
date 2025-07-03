package com.example.rotiku.model

data class Order(
    val customerName: String,
    val productId: String,
    val latitude: Double,
    val longitude: Double
)