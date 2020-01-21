package com.gb.rating.ui

import androidx.lifecycle.*
import com.gb.rating.dataBase.CafeDataSource
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl
import com.gb.rating.models.CafeItem
import com.gb.rating.models.usercase.CafeInteractor
import com.gb.rating.models.utils.MainApplication
import com.gb.rating.ui.settings.BASE_UPDATED_ACTION
import com.gb.rating.ui.settings.OurSearchPropertiesValue
import com.gb.rating.ui.settings.initialSearchProperties
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelMain : ViewModel() {
    var cafeList : MutableLiveData<List<CafeItem>> = MutableLiveData()
    var ourSearchProperties :  MutableLiveData<OurSearchPropertiesValue> = MutableLiveData()
    // CafeInteractor - нужен сразу
    private val db = FirebaseDatabase.getInstance()
    private val repository = Cafe_FB_Impl(db, null)
    var cafeInteractor  = CafeInteractor(repository)

    init {
        val ourSearchPropertiesValue: OurSearchPropertiesValue = initialSearchProperties()
        ourSearchProperties.value = ourSearchPropertiesValue
        refreshCafeList() //ассинхронно, IO
        initDatabaseUpdater(ourSearchPropertiesValue.country, ourSearchPropertiesValue.city) //ассинхронно
    }

    private fun initDatabaseUpdater(country: String, city: String) {
            viewModelScope.launch{
                withContext(Dispatchers.IO){
                    updateInternalDatabase(country, city)
                }
        }
    }

    private fun updateInternalDatabase(country: String = "", city: String= "") {
        cafeInteractor?.retrieveCafeList(country, city)
            ?.subscribe(object : MaybeObserver<List<CafeItem>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(cafeItems: List<CafeItem>) {
                    writeRetrievedCafeListToLocalDatabase(cafeItems) //ассинхронно TODO: разобраться с потоками
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    //empty result - a mistake may be or not
                }
            })
    }

    private fun writeRetrievedCafeListToLocalDatabase(cafeItems: List<CafeItem>) {
        viewModelScope.launch{ //TODO: разобраться с потоками
            val dbHelperW = CafeDataSource(MainApplication.applicationContext())
            dbHelperW.openW()
            dbHelperW.writeCafeList(cafeItems)
            ourSearchProperties.value = ourSearchProperties.value?.updateAction(
                BASE_UPDATED_ACTION)
        }
    }

    fun refreshCafeList() {
        viewModelScope.launch{
        val dbHelperR = CafeDataSource(MainApplication.applicationContext())
        dbHelperR.openR()
        cafeList.value = dbHelperR.readAllCafe()

        }
    }

}



