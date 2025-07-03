package com.example.Rotiku.appwrite

import android.content.Context
import io.appwrite.Client
import io.appwrite.services.Databases

object AppwriteClient {
    private lateinit var client: Client
    lateinit var databases: Databases

    // Ganti dengan kredensial dari Appwrite
    private const val PROJECT_ID = "683d7c58000b12d9fe82"
    private const val ENDPOINT_URL = "https://cloud.appwrite.io/v1"

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint(ENDPOINT_URL)
            .setProject(PROJECT_ID)
            .setSelfSigned(true)

        databases = Databases(client)
    }
}