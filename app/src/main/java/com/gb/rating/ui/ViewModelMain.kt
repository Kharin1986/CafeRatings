package com.gb.rating.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gb.rating.dataBase.CafeDataSource
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl
import com.gb.rating.models.CafeItem
import com.gb.rating.models.usercase.CafeInteractor
import com.gb.rating.models.utils.MainApplication
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import java.util.ArrayList

const val DEFAULT_COUNTRY = "Россия"
const val DEFAULT_CITY = "Москва"
const val COUNTRY_FILTER = "country"
const val CITY_FILTER = "city"

class ViewModelMain : ViewModel() {
    var cafeList : MutableLiveData<List<CafeItem>> = MutableLiveData()
    var ourProperties :  MutableLiveData<Map<String, String>> = MutableLiveData()

    val db = FirebaseDatabase.getInstance()
    val repository = Cafe_FB_Impl(db, null)
    var cafeInteractor  = CafeInteractor(repository)



    fun setList(list: List<CafeItem>) {
        cafeList.value = list
    }

    fun getListCafe() = cafeList

    init {
        initInteractors()
        viewModelScope.launch{
            initDatabaseUpdater()
        }
    }



    private fun initDatabaseUpdater() {
        ourProperties.value = mapOf(COUNTRY_FILTER to DEFAULT_COUNTRY, CITY_FILTER to DEFAULT_CITY)
        var curMap = ourProperties.value as Map<String, String>
        updateInternalDatabase(curMap[COUNTRY_FILTER] as String, curMap[CITY_FILTER] as String )
    }

    fun updateInternalDatabase(country: String = "", city: String= "") {
        val dbHelperW = CafeDataSource(MainApplication.applicationContext())
        dbHelperW.openW()

        if (cafeInteractor == null) {initInteractors()}
        cafeInteractor?.retrieveCafeList(country, city)
            ?.subscribe(object : MaybeObserver<List<CafeItem>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(cafeItems: List<CafeItem>) {
                    dbHelperW.writeCafeList(cafeItems)
                    viewModelScope.launch {
                        refreshCafeList()

                    }
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    //empty result - a mistake may be or not
                }
            })
    }

    public fun refreshCafeList() {
        //retrieveCafeListByType(country, city, "")
        val dbHelperR = CafeDataSource(MainApplication.applicationContext())
        dbHelperR.openR()
        cafeList.value = dbHelperR.readAllCafe()
    }

    private fun initInteractors() {
        val db = FirebaseDatabase.getInstance()
        val repository = Cafe_FB_Impl(db, null)
        cafeInteractor  = CafeInteractor(repository)
    }

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



