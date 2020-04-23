package com.gb.rating.ui

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.gb.rating.dataBase.CafeDataSource
import com.gb.rating.models.*
import com.gb.rating.models.usercase.CafeInteractor
import com.gb.rating.models.utils.MainApplication
import com.gb.rating.models.utils.UpdateLocalCafeList
import io.reactivex.MaybeObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class ViewModelMain (private val cafeInteractor : CafeInteractor) : ViewModel() {
    private val cafeList: MutableLiveData<List<CafeItem>> = MutableLiveData()
    //Kharin 22.02.2020
    private val currentCafeItem: MutableLiveData<List<CafeReviewItem>> = MutableLiveData() //Kharin 22.02.2020
    private val ourSearchProperties: MutableLiveData<OurSearchPropertiesValue> = MutableLiveData()
    // CafeInteractor - нужен сразу
    lateinit var ourSearchPropertiesObserver : Observer<OurSearchPropertiesValue>
    fun cafelist() : LiveData<List<CafeItem>> = cafeList
    //Kharin 22.02.2020
    fun cafeReviewList() : LiveData<List<CafeReviewItem>> = currentCafeItem //Kharin 22.02.2020

    fun ourSearchProperties() : LiveData<OurSearchPropertiesValue> = ourSearchProperties
    fun ourSearchPropertiesValue() : OurSearchPropertiesValue = ourSearchProperties.value!!
    fun ourSearchProperties_update(intValue: OurSearchPropertiesValue) {ourSearchProperties.value = intValue}

    init {
        val ourSearchPropertiesValue: OurSearchPropertiesValue = initOurSearchProperties()
        initDatabaseUpdater(ourSearchPropertiesValue.country, ourSearchPropertiesValue.city,false) //ассинхронно, IO
    }

    private fun initOurSearchProperties(): OurSearchPropertiesValue {
        val ourSearchPropertiesValue: OurSearchPropertiesValue =
            initialSearchProperties()
                .updateAction(INITIATION_ACTION) //не тратим время на старт Активити, помечаем ourSearchPropertiesValue INITIATION_ACTION
        ourSearchProperties.value = ourSearchPropertiesValue
        refreshCafeList() //ассинхронно, IO,
        ourSearchPropertiesObserver = Observer {
            it?.let {
                if (it.action != INITIATION_ACTION)
                    refreshCafeList()
            }
        }
        ourSearchProperties.observeForever(ourSearchPropertiesObserver) // в onCleared() - удаляется
        return ourSearchPropertiesValue
    }

    override fun onCleared() {
        ourSearchProperties.removeObserver(ourSearchPropertiesObserver)
        super.onCleared()
    }

    private fun initDatabaseUpdater(country: String, city: String, removeAll : Boolean = false) {
        UpdateLocalCafeList.updateInternalDatabase(country, city, removeAll)
            .subscribe(object : MaybeObserver<String> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(s: String) {
                    ourSearchProperties.value = ourSearchProperties.value?.updateAction(
                        BASE_UPDATED_ACTION
                    )
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    ourSearchProperties.value = ourSearchProperties.value?.updateAction(
                        BASE_UPDATED_ACTION
                    )
                }
            })
    }

    fun refreshCafeList() {
        val ourSearchPropertiesValue = ourSearchProperties.value;

        if (ourSearchPropertiesValue?.action.equals(RELOAD_DATABASE_ACTION)) {
            initDatabaseUpdater(ourSearchPropertiesValue?.country!!, ourSearchPropertiesValue.city, true)
            return
        }

        viewModelScope.launch {
            cafeList.value =
                withContext(Dispatchers.IO) { readAllCafeWithContext(ourSearchPropertiesValue) }

            if (cafeList.value?.size!! > 0) {
                //временно
                val dbHelperW = CafeDataSource.getInstance(MainApplication.applicationContext())
                dbHelperW.openW()
                dbHelperW.setCafeFav(cafeList.value?.get(0), true)
            }
        }
    }

    fun readAllCafeWithContext(ourSearchPropertiesValue: OurSearchPropertiesValue?): List<CafeItem> {
        val dbHelperR = CafeDataSource.getInstance(MainApplication.applicationContext())
        dbHelperR.openR()
        return dbHelperR.readAllCafe(ourSearchPropertiesValue)
    }

}



