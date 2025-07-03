package com.example.rotiku.MyApplication;

import android.app.Application
import com.example.Rotiku.appwrite.AppwriteClient

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppwriteClient.init(this)
    }
}
