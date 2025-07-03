package com.example.rotiku.repository

import com.example.Rotiku.appwrite.AppwriteClient
import com.example.rotiku.model.Order
import com.example.rotiku.model.Product
import io.appwrite.ID
import io.appwrite.models.Document
import io.appwrite.services.Databases

class ProductRepository {
    private val databases: Databases = AppwriteClient.databases

    // Ganti dengan ID Database dan Collection dari Appwrite
    private val DATABASE_ID = "YOUR_DATABASE_ID"
    private val PRODUCTS_COLLECTION_ID = "products"
    private val ORDERS_COLLECTION_ID = "orders"

    // Mengambil semua produk
    suspend fun getProducts(): List<Product> {
        val response = databases.listDocuments(DATABASE_ID, PRODUCTS_COLLECTION_ID)
        return response.documents.map { it.toProduct() }
    }

    // Mengirim pesanan baru
    suspend fun createOrder(order: Order): Document<Map<String, Any>> {
        val data = mapOf(
            "customerName" to order.customerName,
            "productId" to order.productId,
            "latitude" to order.latitude,
            "longitude" to order.longitude
        )
        return databases.createDocument(
            databaseId = DATABASE_ID,
            collectionId = ORDERS_COLLECTION_ID,
            documentId = ID.unique(),
            data = data
        )
    }

    private fun Document<Map<String, Any>>.toProduct(): Product {
        val data = this.data
        return Product(
            id = this.id,
            name = data["name"] as String,
            price = data["price"] as Double,
            imageUrl = data["imageUrl"] as String
        )
    }
}