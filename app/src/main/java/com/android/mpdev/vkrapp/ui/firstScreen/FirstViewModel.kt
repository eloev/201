package com.android.mpdev.vkrapp.ui.firstScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstViewModel : ViewModel() {

    private val _tag = MutableLiveData<String>().apply {
        value = ""
    }

    val tag: LiveData<String> = _tag

    fun setTagMessage(message: String) {
        _tag.value = message
    }
}