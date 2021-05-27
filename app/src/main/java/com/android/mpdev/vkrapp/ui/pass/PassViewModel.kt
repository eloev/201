package com.android.mpdev.vkrapp.ui.pass

import androidx.lifecycle.ViewModel


private const val TAG = "PassFragment"

class PassViewModel : ViewModel() {
    var passIsVisible = false
    var passIsInit = false

    lateinit var product: Product


    fun database(){

    }
}