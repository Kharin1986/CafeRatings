package com.gb.rating.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.rating.models.CafeItem

class ListViewModel : ViewModel() {

    var cafeList : MutableLiveData<List<CafeItem>> = MutableLiveData()

    //инициализация списка с кафешками
    init {
        //val dbHelper : CafeDataSource = CafeDataSource()
        //dbHelper.openR()
        cafeList.value = TempCafeList.getCafeList()
    }

    fun getListCafe() = cafeList
}