package com.android.mpdev.vkrapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.mpdev.vkrapp.ui.receipt.Receipt
import java.util.*

@Dao
interface ReceiptDao {
    @Query("SELECT * FROM receipt")
    fun getReceipts(): LiveData<List<Receipt>>
    @Query("SELECT * FROM receipt WHERE id=(:id)")
    fun getReceipt(id: UUID): LiveData<Receipt?>
    @Update
    fun updateReceipt(receipt: Receipt)
    @Insert
    fun addReceipt(receipt: Receipt)
}