package com.android.mpdev.vkrapp.ui.secondScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecondViewModel : ViewModel() {

    var isWriteTagOptionOn: Boolean = false
    var messageToSave: String = ""

    private val _text = MutableLiveData<String>().apply {
        value = "Введите текст"
    }
    val text: LiveData<String> = _text

    val _closeDialog = MutableLiveData<Boolean>().apply {
        value = false
    }
    val closeDialog: LiveData<Boolean> = _closeDialog

}