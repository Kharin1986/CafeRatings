package com.gb.rating.models.utils

import androidx.preference.PreferenceManager
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl
import com.gb.rating.models.CafeItem
import com.gb.rating.models.usercase.CafeInteractor
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.math.max

object UpdateLocalCafeList {
    private val cafeInteractor : CafeInteractor by lazy {
        val db = FirebaseDatabase.getInstance()
        val repository = Cafe_FB_Impl(db, null)
        return@lazy CafeInteractor(repository)
    }
    private var cafesLastTimeUpdate: Long

    init {
        val context = MainApplication.applicationContext()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        cafesLastTimeUpdate = prefs.getLong("CafesLastTimeUpdate", 0)

    }

    fun updateInternalDatabase(country: String = "", city: String = "", removeAll : Boolean = false) :Maybe<String> {
        return cafeInteractor.retrieveNewCafeList(country, city, cafesLastTimeUpdate) //с повторением последнего кафе, записанного в последнюю миллисекунду, вдруг еще и другие записались :))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSuccess {
                    cafeItems: List<CafeItem> ->
                cafeInteractor.writeRetrievedCafeListToLocalDatabase(cafeItems, removeAll)
                cafeItems.forEach{cafesLastTimeUpdate = max(cafesLastTimeUpdate, it.changeTime) }
                setprefs()
                }
            .doOnComplete {
                cafeInteractor.writeRetrievedCafeListToLocalDatabase(ArrayList(), removeAll) }
            .map({"Done" }) //более список кафе не нужен, важен только факт окончания
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun setprefs() {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(MainApplication.applicationContext())
        val editor = prefs.edit()
        editor.putLong("CafesLastTimeUpdate", cafesLastTimeUpdate)
        editor.apply()
    }
}