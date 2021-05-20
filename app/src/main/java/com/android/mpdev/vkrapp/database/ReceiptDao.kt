package com.android.mpdev.vkrapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
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
   // @Query("DELETE FROM receipt WHERE id=(:id)")
    //fun deleteReceipt(id: UUID) : LiveData<Receipt?>
}