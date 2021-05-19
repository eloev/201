package com.android.mpdev.vkrapp.ui.receipt

import androidx.lifecycle.ViewModel
import com.android.mpdev.vkrapp.database.ReceiptRepository

class ReceiptViewModel: ViewModel() {
    private val receiptRepository = ReceiptRepository.get()
    val receiptListLiveData = receiptRepository.getReceipts()
}