package com.gb.rating.ui.offline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToOfflineViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is offline Fragment"
    }
    val text: LiveData<String> = _text
}