package com.android.mpdev.vkrapp.ui.receipt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android.mpdev.vkrapp.database.ReceiptRepository
import java.util.*

class ReceiptAddViewModel : ViewModel() {

    private val receiptRepository = ReceiptRepository.get()
    private val receiptIdLiveData = MutableLiveData<UUID>()
    val receiptListLiveData = receiptRepository.getReceipts()

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
}