package com.gb.rating.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.rating.models.CafeItem

class ListViewModel : ViewModel() {

    var cafeList : MutableLiveData<List<CafeItem>> = MutableLiveData()

    //инициализация списка с кафешками
    init {
        cafeList.value = TempCafeList.getCafeList()
    }

    fun getListCafe() = cafeList
}