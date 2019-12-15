package com.gb.rating.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel : ViewModel() {
//
    private val _text = MutableLiveData<String>().apply {
        value = "This is list Fragment"
    }
    val text: LiveData<String> = _text
}