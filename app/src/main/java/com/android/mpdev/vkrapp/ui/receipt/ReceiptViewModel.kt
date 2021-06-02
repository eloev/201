package com.android.mpdev.vkrapp.ui.receipt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.mpdev.vkrapp.database.ReceiptRepository
import java.util.*

private const val TAG = "Receipts"

class ReceiptViewModel: ViewModel() {

    private val receiptIdLiveData = MutableLiveData<UUID>()
    private val receiptRepository = ReceiptRepository.get()
    val receiptListLiveData = receiptRepository.getReceipts()

    var passIsVisible = false
    var passIsInit = false
    var passProduct = ""
    var passPrice = ""

    lateinit var receipt: Receipt
    var receiptInitialized = false

    val receiptLiveData: LiveData<Receipt?> =
        Transformations.switchMap(receiptIdLiveData){
                receiptId -> receiptRepository.getReceipt(receiptId)
        }

    fun loadReceipt(receiptId: UUID){
        receiptIdLiveData.value = receiptId
    }

    fun saveReceipt(receipt: Receipt){
        receiptRepository.updateReceipt(receipt)
    }

    fun addReceipt(receipt: Receipt){
        receiptRepository.addReceipt(receipt)
    }

    fun deleteAllReceipt(){
        receiptRepository.deleteAllReceipt()
    }

    fun deleteReceipt(){
        receiptRepository.deleteReceipt(receipt)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "view model cleared")
    }
}