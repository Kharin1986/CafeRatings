package com.gb.rating.ui

import androidx.lifecycle.*
import com.gb.rating.dataBase.CafeDataSource
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl
import com.gb.rating.models.CafeItem
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions
import com.gb.rating.models.usercase.CafeInteractor
import com.gb.rating.models.utils.MainApplication
import com.gb.rating.ui.settings.*
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.MaybeObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList


class ViewModelMain : ViewModel() {
    private val cafeList: MutableLiveData<List<CafeItem>> = MutableLiveData()
    private val ourSearchProperties: MutableLiveData<OurSearchPropertiesValue> = MutableLiveData()
    // CafeInteractor - нужен сразу
    private val db = FirebaseDatabase.getInstance()
    private val repository = Cafe_FB_Impl(db, null)
    var cafeInteractor = CafeInteractor(repository)

    fun cafelist() : LiveData<List<CafeItem>> = cafeList
    fun ourSearchProperties() : LiveData<OurSearchPropertiesValue> = ourSearchProperties
    fun ourSearchPropertiesValue() : OurSearchPropertiesValue = ourSearchProperties.value!!
    fun ourSearchProperties_update(intValue: OurSearchPropertiesValue) {ourSearchProperties.value = intValue}

    init {
        //db.setPersistenceEnabled(true)
        CommonAuthFunctions.checkAuth()

        val ourSearchPropertiesValue: OurSearchPropertiesValue =
            initialSearchProperties().updateAction(INITIATION_ACTION) //не тратим время на старт Активити, помечаем ourSearchPropertiesValue INITIATION_ACTION
        ourSearchProperties.value = ourSearchPropertiesValue
        refreshCafeList() //ассинхронно, IO,
        initDatabaseUpdater(
            ourSearchPropertiesValue.country,
            ourSearchPropertiesValue.city,
            true
        ) //ассинхронно, IO
    }

    private fun initDatabaseUpdater(country: String, city: String, removeAll : Boolean = false) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateInternalDatabase(country, city, removeAll)
            }
        }
    }

    private fun updateInternalDatabase(country: String = "", city: String = "", removeAll : Boolean = false) {
        cafeInteractor?.retrieveCafeList(country, city)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSuccess {
                    cafeItems -> writeRetrievedCafeListToLocalDatabase(cafeItems, removeAll) }
            .doOnComplete {
                writeRetrievedCafeListToLocalDatabase(ArrayList(), removeAll) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<List<CafeItem>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(cafeItems: List<CafeItem>) {
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

    private fun writeRetrievedCafeListToLocalDatabase(cafeItems: List<CafeItem>, removeAll : Boolean = false) {
        val dbHelperW = CafeDataSource(MainApplication.applicationContext())
        dbHelperW.openW()
        if (removeAll) {dbHelperW.removeAll()}
        dbHelperW.writeCafeList(cafeItems)
    }

    fun refreshCafeList() {
        var ourSearchPropertiesValue = ourSearchProperties.value;

        if (ourSearchPropertiesValue?.action.equals(RELOAD_DATABASE_ACTION)) {

            initDatabaseUpdater(ourSearchPropertiesValue?.country!!, ourSearchPropertiesValue.city, true)

            return
        }

        viewModelScope.launch {
            cafeList.value =
                withContext(Dispatchers.IO) { readAllCafeWithContext(ourSearchPropertiesValue) }

            if (cafeList?.value?.size!! > 0) {
                //временно
                val dbHelperW = CafeDataSource(MainApplication.applicationContext())
                dbHelperW.openW()
                dbHelperW.setCafeFav(cafeList?.value?.get(0), true)
            }
        }
    }

    fun readAllCafeWithContext(ourSearchPropertiesValue: OurSearchPropertiesValue?): List<CafeItem> {
        val dbHelperR = CafeDataSource(MainApplication.applicationContext())
        dbHelperR.openR()
        return dbHelperR.readAllCafe(ourSearchPropertiesValue) //TODO проверить, прямая работа с SQLLite не ведется ли уже в среде IO
    }

}



