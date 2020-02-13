package com.gb.rating.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CafeInfoViewModel : ViewModel() {
    private val cafeID = MutableLiveData<String>().apply {
        value = "This is cafe info Fragment"
    }
    var text: MutableLiveData<String> = cafeID
}
