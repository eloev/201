package com.android.mpdev.vkrapp.database

import android.app.Application

class ReceiptIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ReceiptRepository.initialize(this)
    }
}