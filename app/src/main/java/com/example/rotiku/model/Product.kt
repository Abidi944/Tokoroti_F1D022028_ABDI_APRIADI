package com.example.rotiku.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("\$id")
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String
)