package com.gb.rating.ui.response

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResponseViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is response Fragment"
    }
    val text: LiveData<String> = _text
}