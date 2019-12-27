package com.gb.rating.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.rating.models.CafeItem

class ListViewModel : ViewModel() {

    var cafeList : MutableLiveData<List<CafeItem>> = MutableLiveData()

    fun setList(list: List<CafeItem>) {
        cafeList.value = list
    }

    //инициализация списка с кафешками
//    init {
//        val dbHelper = CafeDataSource(ctx)
//        dbHelper.openR()
//        cafeList.value = dbHelper.readAllCafe()
//    }

    fun getListCafe() = cafeList
}