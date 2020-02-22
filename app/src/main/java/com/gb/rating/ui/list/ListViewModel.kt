package com.gb.rating.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel() : ViewModel() {
    val viewState = MutableLiveData<String>()
}