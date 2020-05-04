package com.gb.rating.models.usercase

import com.gb.rating.dataBase.CafeDataSource
import com.gb.rating.models.CafeItem
import com.gb.rating.models.repository.CafeRepository
import com.gb.rating.models.utils.MainApplication
import io.reactivex.Completable
import io.reactivex.Maybe

class CafeInteractor(private val repository: CafeRepository) {

    @Throws(IllegalArgumentException::class)
    fun writeCafe(cafe: CafeItem): Completable {
        return repository.writeCafe(cafe)
    }

    fun retrieveCafeList(country: String, city: String): Maybe<List<CafeItem>> {
        return repository.retrieveCafeList(country, city)
    }

    fun retrieveNewCafeList(country: String, city: String, changeTime: Long): Maybe<List<CafeItem>> {
        return repository.retrieveNewCafeList(country, city, changeTime )
    }

    fun retrieveCafeListByType(country: String, city: String, type: String): Maybe<List<CafeItem>> {
        return repository.retrieveCafeListByType(country, city, type)
    }

    fun writeRetrievedCafeListToLocalDatabase(cafeItems: List<CafeItem>, removeAll : Boolean = false) {
        val dbHelperW =
            CafeDataSource.getinstanceForServices(MainApplication.applicationContext()) //getApplicationContext()
        dbHelperW.openW()
        if (removeAll) {dbHelperW.removeAll()}
        dbHelperW.writeCafeList(cafeItems)
    }

}
