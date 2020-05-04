package com.gb.rating.ui.review

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.gb.rating.fireBase_RealTime.repository.UnverifiedRating_FB_Impl
import com.gb.rating.models.UnverifiedRating
import com.gb.rating.models.usercase.UnverifiedRatingInteractor
import com.gb.rating.models.utils.MainApplication
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.observers.DisposableCompletableObserver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.logger.KOIN_TAG

class ReviewSharedViewModel : ViewModel() {
    val rateBuilding =
        MutableLiveData<UnverifiedRating?>() //зачем извращаться с псевдонедоступностью - пусть будет изменяемой и доступной всем
    var rateBuildingObserver: Observer<UnverifiedRating?>
    var unverifiedRatingInteractor: UnverifiedRatingInteractor

    init {
        val db = FirebaseDatabase.getInstance()
        val unverifiedRatingRepository = UnverifiedRating_FB_Impl(db, null)
        unverifiedRatingInteractor = UnverifiedRatingInteractor(unverifiedRatingRepository)


        rateBuildingObserver = Observer {
            it?.let {
                if (it.rating > 0.0)
                    unverifiedRatingInteractor.writeRating(it, MainApplication.IMEI()).subscribe(
                        object : DisposableCompletableObserver() {
                            override fun onComplete() {

                            }

                            override fun onError(e: Throwable) {
                                Log.d(
                                    KOIN_TAG,
                                    "Не удалось записать неверифицированный рейтинг: $it: " + e.printStackTrace()
                                )
                            }
                        })
            }
        }
        rateBuilding.observeForever(rateBuildingObserver) // в onCleared() - удаляется

    }

    override fun onCleared() {
        rateBuilding.removeObserver(rateBuildingObserver)
        super.onCleared()
    }

    fun firstPageInfo(intString: String = ""): Boolean {
        //t=20200210T1639&s=527.00&fn=9280440300258540&i=23142&fp=1367589056&n=1
        if (!intString.equals("")) {
            val unverifiedRating = UnverifiedRating()
            val args = intString.split("&").map { it.trim() }
            args.forEach {
                val newArgs = it.split("=")
                if (newArgs.size == 2) {
                    when (newArgs[0]) {
                        "t" -> if (newArgs[1].length == 13) unverifiedRating.fiscalDate = newArgs[1]
                        else -> unverifiedRating.fiscalAttrOthersMap.put(newArgs[0], newArgs[1])
                    }
                }
            }
            unverifiedRating.calculateFicalID()

            if (unverifiedRating.fiscalId.length > 10) {
                MainScope().launch {
                    rateBuilding.value = unverifiedRating
                }
                return@firstPageInfo true
            } else {
                MainScope().launch {
                    rateBuilding.value = null
                }
                return@firstPageInfo false

            }
        } else
            MainScope().launch {
                rateBuilding.value = null
            }
        return@firstPageInfo false
    }

    fun secondPageInfo(
        rateKitchen: Float,
        rateService: Float,
        rateComfort: Float,
        comment: String,
        chosenCafeType: String
    ) {
        val unverifiedRating = rateBuilding.value
        unverifiedRating?.let {
            it.ratingsBaseMap.rateKitchen = rateKitchen
            it.ratingsBaseMap.rateService = rateService
            it.ratingsBaseMap.rateComfort = rateComfort
            it.comment = comment
            it.chosenCafeType = chosenCafeType

            it.calculateRating()
            rateBuilding.value = unverifiedRating
        }
    }
}


