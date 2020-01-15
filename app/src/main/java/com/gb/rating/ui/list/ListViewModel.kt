package com.gb.rating.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl
import com.gb.rating.models.CafeItem
import com.gb.rating.models.usercase.CafeInteractor
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
import java.util.ArrayList

class ListViewModel : ViewModel() {

    var cafeList : MutableLiveData<List<CafeItem>> = MutableLiveData()

    fun setList(list: List<CafeItem>) {
        cafeList.value = list
    }
    fun getListCafe() = cafeList



    fun retrieveCafeListByType(country: String, city: String, type: String) {

        val db = FirebaseDatabase.getInstance()
        val repository = Cafe_FB_Impl(db, null)
        val cafeInteractor = CafeInteractor(repository)

        cafeInteractor.retrieveCafeListByType(country, city, type)
            .subscribe(object : MaybeObserver<List<CafeItem>> {
                override fun onSubscribe(d: Disposable) {}

                override fun onSuccess(cafeItems: List<CafeItem>) {
                    cafeList.setValue(cafeItems)
                }

                override fun onError(e: Throwable) {}

                override fun onComplete() {
                    //empty result
                    cafeList.setValue(ArrayList())
                }
            })
    }
}