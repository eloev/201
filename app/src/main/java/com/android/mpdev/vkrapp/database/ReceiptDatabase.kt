package com.android.mpdev.vkrapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.mpdev.vkrapp.ui.receipt.Receipt

@Database(entities = [ Receipt::class], version=1)
@TypeConverters(ReceiptTypeConverters::class)
abstract class ReceiptDatabase : RoomDatabase() {

    abstract fun receiptDao() : ReceiptDao
}