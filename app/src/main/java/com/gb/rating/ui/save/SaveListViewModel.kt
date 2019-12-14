package com.gb.rating.ui.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SaveListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is SaveList Fragment"
    }
    val text: LiveData<String> = _text
}